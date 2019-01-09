package com.imap.cloud.common.service.school;

import javax.servlet.http.HttpServletRequest;

import com.imap.cloud.common.entity.school.Feedback;
import com.imap.cloud.common.service.base.BaseService;

/**
 * 反馈service
 * @author 冯林
 *
 */
public interface FeedbackService extends BaseService<Feedback, String> {

	int save(Feedback feedback, HttpServletRequest request)throws Exception;

}
