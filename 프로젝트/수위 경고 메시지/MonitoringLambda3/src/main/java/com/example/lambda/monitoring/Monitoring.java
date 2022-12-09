package com.example.lambda.monitoring;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Monitoring implements RequestHandler<Object, String> {

	@Override
	public String handleRequest(Object input, Context context) {
	    context.getLogger().log("Input: " + input);
	    String json = ""+input;
	    JsonParser parser = new JsonParser();
	    JsonElement element = parser.parse(json);
	    JsonElement state = element.getAsJsonObject().get("state");
	    JsonElement reported = state.getAsJsonObject().get("reported");
	    String Water = reported.getAsJsonObject().get("Water").getAsString();
	    double w = Double.valueOf(Water);

	    final String AccessKey="AKIAYNZYVK7Z35OB6POD";
	    final String SecretKey="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";
	    final String topicArn="arn:aws:sns:ap-northeast-1:579402553331:Worning";

	    BasicAWSCredentials awsCreds = new BasicAWSCredentials(AccessKey, SecretKey);  
	    AmazonSNS sns = AmazonSNSClientBuilder.standard()
	                .withRegion(Regions.AP_NORTHEAST_1)
	                .withCredentials( new AWSStaticCredentialsProvider(awsCreds) )
	                .build();

	    
	    final String subject = "Water Level Warning, ";
	    
	    if (w < 800.0) {
	        PublishRequest publishRequest = new PublishRequest(topicArn, subject);
	        PublishResult publishResponse = sns.publish(publishRequest);
	    }

	    return subject+ "Water Level  = " + Water + "!";
	}

}
