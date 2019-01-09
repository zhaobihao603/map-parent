package com.imap.cloud.common.service.map.impl;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.geotools.geojson.geom.GeometryJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.cache.lucene.RedisDirectory;
import com.imap.cloud.common.cache.lucene.io.JedisPoolStream;
import com.imap.cloud.common.cache.lucene.io.JedisStream;
import com.imap.cloud.common.cache.lucene.util.Constants;
import com.imap.cloud.common.entity.map.MapLayer;
import com.imap.cloud.common.entity.system.UnitLabel;
import com.imap.cloud.common.service.map.MapEntityService;
import com.imap.cloud.common.service.map.MapLayerService;
import com.imap.cloud.common.service.map.SearchingService;
import com.imap.cloud.common.service.system.DictionaryService;
import com.imap.cloud.common.service.system.UnitService;
import com.imap.cloud.common.util.IKAnalyzer5x;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

@LogDescription(name="搜索服务")
@Service
public class SearchingServiceImpl implements SearchingService{
	Logger logger = Logger.getLogger(SearchingServiceImpl.class);
    
    private @Autowired MapEntityService mapEntityService;
    
    private @Autowired UnitService unitService;
    
    private @Autowired DictionaryService dictionaryService;
    private @Autowired MapLayerService mapLayerService;
    
    //@Value("${imap.index.path}")
    //public String indexDir;// = "D:/workspace/imap-webapp/WebRoot/index/";
    
    /** 
     * 实例化indexerWriter 
     * @return 
     * @throws Exception 
     */  
    private IndexWriter getWriter(Directory dir)throws Exception{  
        //中文分词器  
    	Analyzer analyzer = new StandardAnalyzer();//analyzer = new IKAnalyzer5x();
          
        IndexWriterConfig cfg=new IndexWriterConfig(analyzer);  
          
        IndexWriter writer=new IndexWriter(dir, cfg);  
          
        return writer;  
    }
    
    @Deprecated
    @LogDescription(name="从数据库里生成实体搜索索引")
	public void buildEntityIndex2(HttpServletRequest request) {
		IndexWriter writer = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/");
			this.indexFileBakTo(path+"index/entity/",path+"index/entity/");
			//获取索引文件目录
			Directory dir = FSDirectory.open(Paths.get(path+"index/entity/"));
			//索引写入器
			writer = this.getWriter(dir);  
			
	        //查询数据
			/*List<MapEntity> list = mapEntityService.findAll();
			for(MapEntity entity:list){ 
				Document doc=new Document();  
				doc.add(new StringField("id", entity.getId(),Field.Store.YES));
	            doc.add(new StringField("title",defaultForEmpty(entity.getName()),Field.Store.YES));
	            //doc.add(new StringField("imagePath",entity.getName(),Field.Store.YES));
	            doc.add(new DoubleField("x",entity.getX(),Field.Store.YES)); 
	            doc.add(new DoubleField("y",entity.getY(),Field.Store.YES)); 
	            doc.add(new DoubleField("areaId",entity.getArea()==null?26l:entity.getArea(),Field.Store.YES));
				//doc.add(new StringField("area",item.getValue(),Field.Store.YES));
	            doc.add(new StringField("address",defaultForEmpty(entity.getAddress()),Field.Store.YES));  
	            doc.add(new TextField("desc", defaultForEmpty(entity.getDescription()), Field.Store.YES));  
	            writer.addDocument(doc);   
			}*/
//			writer.forceMerge(maxNumSegments)
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    @LogDescription(name="生成实体搜索索引")
	@Override
	public void buildEntityIndex(HttpServletRequest request) {
		IndexWriter writer = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/");
			this.indexFileBakTo(path+"index/entity/",path+"index/entity/");
			//获取索引文件目录
			Directory dir = FSDirectory.open(Paths.get(path+"index/entity/"));
			//索引写入器
			writer = this.getWriter(dir);  
			
			String str = mapEntityService.selectAllFromWFS();
			JSONObject json = JSON.parseObject(str);
			JSONArray features = json.getJSONArray("features");
			List<MapLayer> list = mapLayerService.getBaseMap();
			for (int i=0;i<features.size();i++) { 
		    	  JSONObject feature = features.getJSONObject(i); 
		    	  JSONObject properties = feature.getJSONObject("properties");
			      GeometryJSON gjson = new GeometryJSON();  
			      Reader reader = new StringReader(feature.getString("geometry"));
			      Geometry geo = gjson.read(reader);
			      Point center = geo.getCentroid();			      
			      if(!properties.containsKey("fid")){
			    	  String fid = feature.getString("id").substring(feature.getString("id").indexOf(".")).replace(".", "");
			    	  properties.put("fid", fid);
			      }
			      Document doc=new Document();  
					doc.add(new StringField("id", defaultForEmpty(properties.getString("fid")),Field.Store.YES));
		            doc.add(new StringField("title",defaultForEmpty(properties.getString("name")),Field.Store.YES));
		            doc.add(new DoubleField("x",center.getX(),Field.Store.YES)); 
		            doc.add(new DoubleField("y",center.getY(),Field.Store.YES));
		            doc.add(new StringField("address",defaultForEmpty(properties.getString("address")),Field.Store.YES));
		            doc.add(new StringField("enAddress",defaultForEmpty(properties.getString("enAddress")),Field.Store.YES)); 
		            doc.add(new TextField("desc", defaultForEmpty(properties.getString("remark")), Field.Store.YES)); 
		            doc.add(new StringField("ename",defaultForEmpty(properties.getString("ename")),Field.Store.YES));
		            doc.add(new StringField("shortname",defaultForEmpty(properties.getString("alias")),Field.Store.YES));
		            boolean added = false;
		            if(list!=null){
		            	//for(DictionaryItem item:list){
		            	for(MapLayer item:list){
		            		//if(item.getValue().indexOf(properties.getString("sname"))>-1){
		            		if(item.getName().indexOf(properties.getString("sname"))>-1){
		            			doc.add(new StringField("areaId",item.getId(),Field.Store.YES));
		            			doc.add(new StringField("area",item.getName(),Field.Store.YES));
		            			doc.add(new StringField("englishArea",item.getEname(),Field.Store.YES));
		            			added = true;
		            			break;
		            		}
		            	}
		            }
		            if(!added)doc.add(new LongField("areaId",26l,Field.Store.YES));
		            writer.addDocument(doc); 
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    @LogDescription(name="生成单位搜索索引")
	@Override
	public void buildUnitIndex(HttpServletRequest request) {
		IndexWriter writer = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/");
			this.indexFileBakTo(path+"index/unit/",path+"index/unit/");
			//获取索引文件目录
			Directory dir = FSDirectory.open(Paths.get(path+"index/unit/"));
			//索引写入器
			writer = this.getWriter(dir);  
	        //查询数据
			List<UnitLabel> list = unitService.listUnitLabelsByConditions(null);
			for(UnitLabel entity:list){ 
				if(entity == null || entity.getArea() ==null
						||entity.getY() ==null || entity.getX()==null) continue;
				Document doc=new Document();  
				doc.add(new StringField("id", entity.getId(),Field.Store.YES));
	            doc.add(new StringField("title",defaultForEmpty(entity.getName()),Field.Store.YES));
	            doc.add(new DoubleField("x",entity.getX(),Field.Store.YES)); 
	            doc.add(new DoubleField("y",entity.getY(),Field.Store.YES));
	            doc.add(new StringField("area",entity.getArea()==null?"":entity.getArea().getName(),Field.Store.YES));
	            doc.add(new StringField("englishArea",entity.getArea()==null?"":entity.getArea().getEname(),Field.Store.YES));
	            doc.add(new StringField("areaId",entity.getArea()==null?"26":entity.getArea().getId(),Field.Store.YES)); 
	            doc.add(new StringField("address",defaultForEmpty(entity.getAddress()),Field.Store.YES));
	            doc.add(new StringField("enAddress",defaultForEmpty(entity.getEnAddress()),Field.Store.YES));
	            doc.add(new TextField("desc", defaultForEmpty(entity.getDescription()), Field.Store.YES));
	            doc.add(new StringField("ename",defaultForEmpty(entity.getEnglishName()),Field.Store.YES));
	            doc.add(new StringField("shortname",defaultForEmpty(entity.getName()),Field.Store.YES));
	            writer.addDocument(doc);   
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
    @Override
	public void buildEntityUnitIndexByRedis(HttpServletRequest request) {
    	IndexWriter writer = null;
    	RedisDirectory redisDirectory = null;
    	//jedis打开
    	JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, Constants.TIME_OUT,"123456");
    	try {
    		redisDirectory = new RedisDirectory(new JedisPoolStream(jedisPool));
			//先把redis索引旧数据删
			String nameArr[] = redisDirectory.listAll();
			for (int i = 0; i < nameArr.length; i++) {
				redisDirectory.deleteFile(nameArr[i]);
			}
			
			//索引写入器
			writer = this.getWriter(redisDirectory);
			
			//实体索引
			String str = mapEntityService.selectAllFromWFS();
			JSONObject json = JSON.parseObject(str);
			JSONArray features = json.getJSONArray("features");
			List<MapLayer> list = mapLayerService.getBaseMap();
			for (int i=0;i<features.size();i++) { 
		    	  JSONObject feature = features.getJSONObject(i); 
		    	  JSONObject properties = feature.getJSONObject("properties");
			      GeometryJSON gjson = new GeometryJSON();  
			      Reader reader = new StringReader(feature.getString("geometry"));
			      Geometry geo = gjson.read(reader);
			      Point center = geo.getCentroid();
			      
			      Document doc=new Document();  
					doc.add(new StringField("id", defaultForEmpty(properties.getString("fid")),Field.Store.YES));
		            doc.add(new StringField("title",defaultForEmpty(properties.getString("name")),Field.Store.YES));
		            doc.add(new DoubleField("x",center.getX(),Field.Store.YES)); 
		            doc.add(new DoubleField("y",center.getY(),Field.Store.YES));
		            doc.add(new StringField("address",defaultForEmpty(properties.getString("address")),Field.Store.YES));
		            doc.add(new StringField("enAddress",defaultForEmpty(properties.getString("enAddress")),Field.Store.YES)); 
		            doc.add(new TextField("desc", defaultForEmpty(properties.getString("remark")), Field.Store.YES)); 
		            doc.add(new StringField("ename",defaultForEmpty(properties.getString("ename")),Field.Store.YES));
		            doc.add(new StringField("shortname",defaultForEmpty(properties.getString("alias")),Field.Store.YES));
		            boolean added = false;
		            if(list!=null){
		            	for(MapLayer item:list){
		            		if(item.getName().indexOf(properties.getString("sname"))>-1){
		            			doc.add(new StringField("areaId",item.getId(),Field.Store.YES));
		            			doc.add(new StringField("area",item.getName(),Field.Store.YES));
		            			doc.add(new StringField("englishArea",item.getEname(),Field.Store.YES));
		            			added = true;
		            			break;
		            		}
		            	}
		            }
		            if(!added)doc.add(new LongField("areaId",26l,Field.Store.YES));
		            writer.addDocument(doc); 
			}
			
			//单位索引
			List<UnitLabel> list1 = unitService.listUnitLabelsByConditions(null);
			for(UnitLabel entity:list1){ 
				if(entity == null || entity.getArea() ==null
						||entity.getY() ==null || entity.getX()==null) continue;
				Document doc=new Document();  
				doc.add(new StringField("id", entity.getId(),Field.Store.YES));
	            doc.add(new StringField("title",defaultForEmpty(entity.getName()),Field.Store.YES));
	            doc.add(new DoubleField("x",entity.getX(),Field.Store.YES)); 
	            doc.add(new DoubleField("y",entity.getY(),Field.Store.YES));
	            doc.add(new StringField("area",entity.getArea()==null?"":entity.getArea().getName(),Field.Store.YES));
	            doc.add(new StringField("englishArea",entity.getArea()==null?"":entity.getArea().getEname(),Field.Store.YES));
	            doc.add(new StringField("areaId",entity.getArea()==null?"26":entity.getArea().getId(),Field.Store.YES)); 
	            doc.add(new StringField("address",defaultForEmpty(entity.getAddress()),Field.Store.YES));
	            doc.add(new StringField("enAddress",defaultForEmpty(entity.getEnAddress()),Field.Store.YES));
	            doc.add(new TextField("desc", defaultForEmpty(entity.getDescription()), Field.Store.YES));
	            doc.add(new StringField("ename",defaultForEmpty(entity.getEnglishName()),Field.Store.YES));
	            doc.add(new StringField("shortname",defaultForEmpty(entity.getName()),Field.Store.YES));
	            writer.addDocument(doc);   
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				writer.close();
				redisDirectory.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
	/**
	 * 当为null时，设置为空字符串
	 * Lucene不能设置field为null
	 * @param field
	 * @return
	 */
	private String defaultForEmpty(String field){
		return field==null?"":field;
	}
	
	//多索引目录
	@LogDescription(name="实体和单位搜索")
	public Map<String,Object> search(String keyWord,Integer areaId, Integer pageSize,Integer currentPage,HttpServletRequest request) throws IOException, ParseException, InvalidTokenOffsetsException{
        String path = request.getSession().getServletContext().getRealPath("/");
		Directory directory1 = FSDirectory.open(Paths.get(path+"index/unit/"));  
        Directory directory2 = FSDirectory.open(Paths.get(path+"index/entity/"));  
        IndexReader reader1 = DirectoryReader.open(directory1);  
        IndexReader reader2 = DirectoryReader.open(directory2);  
        MultiReader multiReader = new MultiReader(reader1,reader2);
        // 1、目录搜索器
        IndexSearcher indexSearcher = new IndexSearcher(multiReader); 
        
        // 2、搜索解析器  
        //SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        /*Analyzer analyzer = new IKAnalyzer5x();
        QueryParser parser = new MultiFieldQueryParser( new String[]{"title","address","desc"}, analyzer);
	    Query query1 = parser.parse(keyWord);*/
	    //Query query = new FuzzyQuery(new Term("title", keyWord),2);
	    WildcardQuery query = new WildcardQuery(new Term("title", "*"+keyWord+"*")); 
	    Term term2 = new Term("ename", "*"+keyWord+"*");
	    TermQuery termQuery = new TermQuery(term2); 
	    Query andQuery = null;
	    if(areaId!=null && areaId>0){
	    	BooleanQuery.Builder builder = new BooleanQuery.Builder();
	    	builder.add( query, Occur.MUST );
	    	builder.add( termQuery, Occur.MUST );
			WildcardQuery query2 = new WildcardQuery(new Term("areaId", "*"+areaId+"*"));  
	    	//Query query2 = NumericRangeQuery.newLongRange("areaId", Long.valueOf(areaId), Long.valueOf(areaId), true, true);
	    	builder.add( query2, Occur.MUST );
	    	andQuery = builder.build();
	    }
        // 4、分页
        TopDocs topDocs = indexSearcher.search( andQuery==null?query:andQuery,1000);        
        int totalCount = topDocs.totalHits; // 搜索结果总数量  
        ScoreDoc[] scoreDocs = topDocs.scoreDocs; // 搜索返回的结果集合
        
        pageSize = pageSize == null || pageSize <= 0 ?1000 : pageSize;
        currentPage = currentPage == null || currentPage <= 0? 1 : currentPage;
        //查询起始记录位置  
        int begin = pageSize * (currentPage - 1) ;  
        //查询终止记录位置  
        int end = Math.min(begin + pageSize, scoreDocs.length);  
        
        List<Map<String,Object>> recordList = new ArrayList<Map<String,Object>>(); 
    	//进行分页查询  
        for(int i=begin;i<end;i++) {
            int docID = scoreDocs[i].doc;  
            Document doc = indexSearcher.doc(docID); // 获取搜索结果
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id", doc.get("id"));  
            map.put("title", doc.get("title"));
            map.put("address", doc.get("address"));
            map.put("x", doc.get("x"));
            map.put("y", doc.get("y"));
            map.put("areaId", doc.get("areaId"));
            map.put("area", doc.get("area"));
            recordList.add(map);
        }
        
        Map<String,Object> page = new HashMap<String,Object>();
        page.put("totalCount", totalCount);
        page.put("currentPage", currentPage);
        page.put("pageSize", pageSize);
        page.put("pages", (totalCount+(pageSize-1))/pageSize);
        page.put("list", recordList);
        multiReader.close();
        return page;
	}

	//多索引目录
	@LogDescription(name="实体和单位搜索")
	public Map<String,Object> search(String keyWord,String areaId, Integer pageSize,Integer currentPage,HttpServletRequest request) throws IOException, ParseException, InvalidTokenOffsetsException{
		String path = request.getSession().getServletContext().getRealPath("/");
		Directory directory1 = FSDirectory.open(Paths.get(path+"index/unit/"));
		Directory directory2 = FSDirectory.open(Paths.get(path+"index/entity/"));
		IndexReader reader1 = DirectoryReader.open(directory1);
		IndexReader reader2 = DirectoryReader.open(directory2);
		MultiReader multiReader = new MultiReader(reader1,reader2);
		// 1、目录搜索器
		IndexSearcher indexSearcher = new IndexSearcher(multiReader);

		// 2、搜索解析器
		WildcardQuery query = new WildcardQuery(new Term("title", "*"+keyWord+"*"));
		// 4、分页
		TopDocs topDocs = indexSearcher.search( query,1000);
		int totalCount = topDocs.totalHits; // 搜索结果总数量
		ScoreDoc[] scoreDocs = topDocs.scoreDocs; // 搜索返回的结果集合

		pageSize = pageSize == null || pageSize <= 0 ?1000 : pageSize;
		currentPage = currentPage == null || currentPage <= 0? 1 : currentPage;
		//查询起始记录位置
		int begin = pageSize * (currentPage - 1) ;
		//查询终止记录位置
		int end = Math.min(begin + pageSize, scoreDocs.length);

		List<Map<String,Object>> recordList = new ArrayList<Map<String,Object>>();
		//进行分页查询
		for(int i=begin;i<end;i++) {
			int docID = scoreDocs[i].doc;
			Document doc = indexSearcher.doc(docID); // 获取搜索结果
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", doc.get("id"));
			map.put("title", doc.get("title"));
			map.put("address", doc.get("address"));
			map.put("x", doc.get("x"));
			map.put("y", doc.get("y"));
			String aid = doc.get("areaId");
			map.put("areaId", aid);
			map.put("area", doc.get("area"));
			map.put("ename", doc.get("ename"));
			if(!StringUtils.isEmpty(areaId) && aid.equals(areaId)){
				recordList.add(0,map);
			}else{
				recordList.add(map);
			}
		}
		recordList = this.duplicateEle(recordList);
		Map<String,Object> page = new HashMap<String,Object>();
		page.put("totalCount", totalCount);
		page.put("currentPage", currentPage);
		page.put("pageSize", pageSize);
		page.put("pages", (totalCount+(pageSize-1))/pageSize);
		page.put("list", recordList);
		multiReader.close();
		return page;
	}

	//去掉相同记录
	private List<Map<String,Object>> duplicateEle(List<Map<String,Object>> recordList){
		List<Map<String,Object>> list = recordList;
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			String id = map.get("id").toString();
			String title = map.get("title").toString();
			String areaId = map.get("areaId").toString();
			for (int j = i+1; j < list.size(); j++) {
				Map<String,Object> map2 = list.get(j);
				String id2 = map2.get("id").toString();
				if(id.equals(id2)){
					list.remove(j);
					continue;
				}
				String title2 = map2.get("title").toString();
				String areaId2 = map2.get("areaId").toString();
				if(title.equals(title2) && areaId.equals(areaId2)){
					list.remove(j);
					continue;
				}
			}
		}
		return list;
	}
	
	@Override
	public Map<String,Object> searchToRedis(String keyWord, String areaId, Integer pageSize,
			Integer currentPage, HttpServletRequest request) {
		Map<String,Object> page = new HashMap<String,Object>();
		RedisDirectory redisDirectory = null;
		try {
			redisDirectory = new RedisDirectory(new JedisStream("127.0.0.1",6379,1000,"123456"));
			IndexReader reader = DirectoryReader.open(redisDirectory);  
			MultiReader multiReader = new MultiReader(reader);
			// 1、目录搜索器
	        IndexSearcher indexSearcher = new IndexSearcher(reader);
	        // 2、搜索解析器  
		    WildcardQuery query = new WildcardQuery(new Term("title", "*"+keyWord+"*")); 
	        // 4、分页
	        TopDocs topDocs = indexSearcher.search( query,1000);        
	        int totalCount = topDocs.totalHits; // 搜索结果总数量  
	        ScoreDoc[] scoreDocs = topDocs.scoreDocs; // 搜索返回的结果集合
	        
	        pageSize = pageSize == null || pageSize <= 0 ?1000 : pageSize;
	        currentPage = currentPage == null || currentPage <= 0? 1 : currentPage;
	        //查询起始记录位置  
	        int begin = pageSize * (currentPage - 1) ;  
	        //查询终止记录位置  
	        int end = Math.min(begin + pageSize, scoreDocs.length);  
	        
	        List<Map<String,Object>> recordList = new ArrayList<Map<String,Object>>();
	    	//进行分页查询  
	        for(int i=begin;i<end;i++) {
	            int docID = scoreDocs[i].doc;  
	            Document doc = indexSearcher.doc(docID); // 获取搜索结果
	            Map<String,Object> map = new HashMap<String,Object>();
	            map.put("id", doc.get("id"));  
	            map.put("title", doc.get("title"));
	            map.put("address", doc.get("address"));
	            map.put("x", doc.get("x"));
	            map.put("y", doc.get("y"));
	            String aid = doc.get("areaId");
	            map.put("areaId", aid);
	            map.put("area", doc.get("area"));
	            map.put("ename", doc.get("ename"));
	            if(!StringUtils.isEmpty(areaId) && aid.equals(areaId)){
	            	recordList.add(0,map);
	            }else{
	            	recordList.add(map);
	            }
	        }
	        
	        
	        page.put("totalCount", totalCount);
	        page.put("currentPage", currentPage);
	        page.put("pageSize", pageSize);
	        page.put("pages", (totalCount+(pageSize-1))/pageSize);
	        page.put("list", recordList);
	        multiReader.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				redisDirectory.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return page;
	}
	
	/**
	 * 将from下的文件备份到to目录下
	 * @param from
	 * @param to
	 */
	private void indexFileBakTo(String from, String to){
		try{
			File source = new File(from);
			if(from.equals(to)){
				SimpleDateFormat df=new SimpleDateFormat("yyyyMMddhhmmss");
				to += "bak";
				to += df.format(new Date());
			}
			File dest = new File(to); 
			if(source.isDirectory()){
				if (!dest.exists()) {  
					dest.mkdirs();  
                }
				File[] files = source.listFiles();// 将文件或文件夹放入文件集  
	            if (files == null)// 判断文件集是否为空  
	                return;
	            for (int i = 0; i < files.length; i++) {// 遍历文件集  
	                if (files[i].isDirectory()) {//如果是文件夹或目录,跳过  
	                    continue;
	                }  
	                // 将文件目录放入移动后的目录  
	                File moveFile = new File(dest.getPath() + File.separatorChar+ files[i].getName()); 
	                         
	                if (moveFile.exists()) {// 目标文件夹下存在的话，删除  
	                    moveFile.delete();  
	                }  
	                files[i].renameTo(moveFile);// 移动文件  
	            }  
			}
		}catch(Exception e){
			logger.info("索引文件备份出错...");
		}
	}
	
	/******************************************** 以下为测试代码  **********************************************/
	static void LuceneCreateIndex( String indexDir,boolean aa) throws Exception
	   {
	      // IKAnalyzer 有独特之处，同时支持多种语言的分词
		  Analyzer analyzer = new IKAnalyzer5x();
	      Directory directory = FSDirectory.open( Paths.get( indexDir ) );
	      IndexWriterConfig indexWriterConfig = new IndexWriterConfig( analyzer );
	      IndexWriter indexWriter = new IndexWriter( directory, indexWriterConfig );
	      if(aa){
		      Document document = new Document();
		      document.add( new TextField( "desc", "北京地铁亦庄线（以下简称“亦庄线”），是北京地铁5号线向南的延长线，连接北京市区与北京经济技术开发区，由北京市地铁运营有限公司一分公司负责运营。" , Field.Store.YES) );
		      document.add( new TextField( "title", "北京地铁亦庄线", Field.Store.YES ) );
		      document.add( new TextField( "address", "2010年12月30日", Field.Store.YES ) );
		      indexWriter.addDocument( document );
		      
		      document = new Document();
		      document.add( new TextField( "desc", "北京地铁10号线（以下简称“10号线”），是北京的第二条环线地铁，为北京地铁线网中非常重要的一条线路，其一部分路段与北京三环重叠，也是目前北京地铁系统中最繁忙的线路。" , Field.Store.YES) );
		      document.add( new TextField( "title", "北京地铁10号线", Field.Store.YES ) );
		      document.add( new TextField( "address", "2008年7月19日", Field.Store.YES ) );
		      indexWriter.addDocument( document );
		      
		      document = new Document();
		      document.add( new TextField( "desc", "地铁17号线位于北京市东部地区，是一条贯穿中心城南北方向的轨道交通干线，其定位为：大运量等级快线。 " , Field.Store.YES) );
		      document.add( new TextField( "title", "北京地铁17号线", Field.Store.YES ) );
		      document.add( new TextField( "address", "2019年底", Field.Store.YES ) );
		      indexWriter.addDocument( document );
	      }else{
	    	  Document document = new Document();
	    	  document.add( new TextField( "desc", "亚伯·塔斯曼，Abel Tasman，1603年～1659年10月10日，荷兰探险家、航海家、商人。" , Field.Store.YES) );
		      document.add( new TextField( "title", "著名荷兰航海家亚伯·塔斯曼发现新西兰", Field.Store.YES ) );
		      document.add( new TextField( "address", "1642年", Field.Store.YES ) );
		      indexWriter.addDocument( document );
		      
		      document = new Document();
		      document.add( new TextField( "desc", "郑燮（1693—1765年），字克柔，号板桥。自称板桥居士，清代画家、文学家。汉族，江苏兴化人。" , Field.Store.YES) );
		      document.add( new TextField( "title", "著名中国清代画家郑板桥逝世", Field.Store.YES ) );
		      document.add( new TextField( "address", "1765年", Field.Store.YES ) );
		      indexWriter.addDocument( document );
		      
		      document = new Document();
		      document.add( new TextField( "desc", "居斯塔夫·福楼拜，（1821-1880）19世纪中期法国伟大的批判现实主义小说家，居伊·德·莫泊桑就曾拜他为师。" , Field.Store.YES) );
		      document.add( new TextField( "title", "法国著名作家居斯塔夫·福楼拜出生", Field.Store.YES ) );
		      document.add( new TextField( "address", "1821年", Field.Store.YES ) );
		      indexWriter.addDocument( document );
	      }
	      indexWriter.close();
	   }
	
	/**
	 * 多目录多字段查询
	 * @param indexDir
	 * @param q
	 * @throws Exception
	 */
	static void search(String q) throws Exception{
		Directory directory1 = FSDirectory.open(Paths.get("D:/workspace/imap-webapp/WebRoot/index/unit/"));  
        Directory directory2 = FSDirectory.open(Paths.get("D:/workspace/imap-webapp/WebRoot/index/entity/"));  
        IndexReader reader1 = DirectoryReader.open(directory1);  
        IndexReader reader2 = DirectoryReader.open(directory2);  
        MultiReader multiReader = new MultiReader(reader1,reader2);
        // 1、目录搜索器
        IndexSearcher indexSearcher = new IndexSearcher(multiReader); 
        
        // 2、搜索解析器  
        //SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        Analyzer analyzer = new IKAnalyzer5x();
        QueryParser parser = new MultiFieldQueryParser( new String[]{"title","address","desc"}, analyzer);
	    Query query = parser.parse(q);
	    
        // 4、分页
        TopDocs topDocs = indexSearcher.search( query,1000);        
        int totalCount = topDocs.totalHits; // 搜索结果总数量  
        System.out.println("搜索关键字:"+q+",共"+totalCount+"条结果");
		System.out.println("+++++++++++++++++++++++++++++++++++++++");
        ScoreDoc[] scoreDocs = topDocs.scoreDocs; // 搜索返回的结果集合
        
        //准备高亮器  
        Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");  
        Scorer scorer = new QueryScorer(query);  
        Highlighter highlighter = new Highlighter(formatter, scorer);  
        Fragmenter fragmenter = new SimpleFragmenter(50);  
        highlighter.setTextFragmenter(fragmenter);
        
        for(ScoreDoc scoreDoc : topDocs.scoreDocs) {  
        	// 获取搜索结果
	        Document document = indexSearcher.doc( scoreDoc.doc );
	        // 返回高亮后的结果，如果当前属性值中没有出现关键字，会返回 null  
            String hc = highlighter.getBestFragment(analyzer, "desc", document.get("desc"));  
            if (hc == null) {  
                String content = document.get("desc");  
                int endIndex = Math.min(50, content.length());  
                hc = content.substring(0, endIndex);// 最多前50个字符  
            }  
	            
	         System.out.println( document.get( "title" ) );
	         System.out.println( document.get( "address" ) );
	         System.out.println( hc );
	         System.out.println("+++++++++++++++++++++++++++++++++++++++");
        }
	}
	
	/**
	 * 单目录多字段查询
	 * @param indexDir
	 * @param q
	 * @throws Exception
	 */
	static void search( String indexDir, String q ) throws Exception{
	      Directory directory = FSDirectory.open( Paths.get( indexDir ) );
	      IndexReader indexReader = DirectoryReader.open( directory );
	      IndexSearcher indexSearcher = new IndexSearcher( indexReader );
	      Analyzer analyzer = new IKAnalyzer5x();
	      
	      QueryParser mparser = new MultiFieldQueryParser( new String[]{"title","address","desc"}, analyzer);
	      Query query = mparser.parse(q);
	      /*// 搜索目标是 contents
	      QueryParser parser = new QueryParser( "fileName", analyzer );
	      // 传入关键字，进行分析
	      Query query = parser.parse( q );*/
	      // 分页，这里取前十个
	      TopDocs topDocs = indexSearcher.search( query, 10 );
	      System.out.println(topDocs.totalHits);
	      for ( ScoreDoc scoreDoc : topDocs.scoreDocs ){
	         // 获取搜索结果
	         Document document = indexSearcher.doc( scoreDoc.doc );
	         System.out.println( document.get( "address" ) );
	         System.out.println( document.get( "desc" ) );
	         System.out.println("+++++++++++++++++++++++++++++++++++++++");
	      }

	      indexReader.close();
	   }
	public static void load() throws IOException{
		Directory directory1 = FSDirectory.open(Paths.get("D:/workspace/jinandaxue-webapp/WebRoot/index/unit/"));  
        Directory directory2 = FSDirectory.open(Paths.get("D:/workspace/jinandaxue-webapp/WebRoot/index/entity/"));  
        IndexReader reader1 = DirectoryReader.open(directory1);  
        IndexReader reader2 = DirectoryReader.open(directory2);  
        MultiReader multiReader = new MultiReader(reader1,reader2);
        // 1、目录搜索器
        IndexSearcher indexSearcher = new IndexSearcher(multiReader); 
        Query query1 = new WildcardQuery(new Term("title", "*浙江*")); 
        //Query query2=new TermQuery(new Term("areaId","27")); 
        Query query2 = NumericRangeQuery.newLongRange("areaId", 28L, 28L, true, true);
        //核心句  
        BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();  
          
        //把多条件查询的query都加到BooleanQuery中去  
        booleanQuery.add(query1,Occur.MUST);  
        booleanQuery.add(query2,Occur.MUST);
        TopDocs hits=indexSearcher.search(booleanQuery.build(), 10);  
        
        for(ScoreDoc scoreDoc:hits.scoreDocs){  
            Document doc=indexSearcher.doc(scoreDoc.doc);  
              
            System.out.println(doc.get("id"));  
            System.out.println(doc.get("title")); 
            System.out.println(doc.get("x"));
            System.out.println(doc.get("y"));
            System.out.println(doc.get("areaId"));
            System.out.println(doc.get("address"));
        }   
	}
	
	public static void main(String[] args) throws Exception {
		//SearchingServiceImpl.createIndex();
		//SearchingServiceImpl.search("D:/workspace/imap-webapp/WebRoot/index/aa/","南区教工生活区9栋");
		//SearchingServiceImpl.LuceneCreateIndex("D:/workspace/imap-webapp/WebRoot/index/aa/",true);
		//SearchingServiceImpl.LuceneCreateIndex("D:/workspace/imap-webapp/WebRoot/index/bb/",false);
		//SearchingServiceImpl.search("D:/workspace/imap-webapp/WebRoot/index/unit/","测试");
		SearchingServiceImpl.load();
		//SearchingServiceImpl.search("学院");
	}

}
