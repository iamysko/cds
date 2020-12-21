package com.misterveiga.cds.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.data.CdsData;

@Component
public class MessageFilter {

	@Autowired
	CdsData cdsData; // TODO: Add filtered regex expressions to MongoDB (OR file on server).

	public static boolean filterMessage() {
		return true; // TODO: Implement message filter.
	}

}
