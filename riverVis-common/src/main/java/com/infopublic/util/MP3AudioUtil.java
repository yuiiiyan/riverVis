package com.infopublic.util;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import java.io.IOException;

public class MP3AudioUtil {
	
	public static void getAudioHeader(String mp3Path){
		try {
			MP3File mp3File = new MP3File(mp3Path);//封装好的类
			MP3AudioHeader header = mp3File.getMP3AudioHeader();
			System.out.println("时长: " + header.getTrackLength()); //获得时长
			System.out.println("比特率: " + header.getBitRate()); //获得比特率
			System.out.println("音轨长度: " + header.getTrackLength()); //音轨长度
			System.out.println("格式: " + header.getFormat()); //格式，例 MPEG-1
			System.out.println("声道: " + header.getChannels()); //声道
			System.out.println("采样率: " + header.getSampleRate()); //采样率
			System.out.println("MPEG: " + header.getMpegLayer()); //MPEG
			System.out.println("MP3起始字节: " + header.getMp3StartByte()); //MP3起始字节
			System.out.println("精确的音轨长度: " + header.getPreciseTrackLength()); //精确的音轨长度
		} catch (Exception e) {
			System.out.println("没有获取到任何信息");
		}
	}
	
	public static String getAudioBitRate(String mp3Path){
		MP3File mp3File = null;
		try {
			mp3File = new MP3File(mp3Path);
		} catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
			e.printStackTrace();
			return null;
		}//封装好的类
		MP3AudioHeader header = mp3File.getMP3AudioHeader();
		return header.getBitRate();
	}
}
