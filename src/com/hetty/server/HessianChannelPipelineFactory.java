package com.hetty.server;

import static org.jboss.netty.channel.Channels.pipeline;

import java.util.concurrent.ExecutorService;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public class HessianChannelPipelineFactory implements ChannelPipelineFactory {

	private ExecutorService threadpool;
	
	public HessianChannelPipelineFactory(ExecutorService threadpool){
		this.threadpool=threadpool;
	}
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();

		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("handler", new HessianProtocolHandler(threadpool));
		return pipeline;
	}

}
