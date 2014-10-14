package vstreaming.videoconcate.ffmpeg;
import java.util.LinkedList;
import java.util.List;

import android.text.TextUtils;


public class FfmpegJob {

	public String inputPath1;
	public String inputPath2;
	
	public String outputPath;
	
	private final String mFfmpegPath;
	
	public FfmpegJob(String ffmpegPath) {
		mFfmpegPath = ffmpegPath;
	}
	
	public ProcessRunnable create() {
		if (inputPath1 == null || inputPath2 == null || outputPath == null) {
			throw new IllegalStateException("Need an input and output filepath!");
		}	
		
		final List<String> cmd = new LinkedList<String>();
		
		cmd.add(mFfmpegPath);
		cmd.add("-i");
		cmd.add(inputPath1);
		cmd.add("-vcodec");
		cmd.add("copy");
		cmd.add("-acodec");
		cmd.add("copy");
		cmd.add("-vbsf");
		cmd.add("h264_mp4toannexb");
		cmd.add("/mnt/sdcard/input1.ts");
		
		final ProcessBuilder pb = new ProcessBuilder(cmd);
		new ProcessRunnable(pb).run();
		
		final List<String> cmd1 = new LinkedList<String>();
		
		cmd1.add(mFfmpegPath);
		cmd1.add("-i");
		cmd1.add(inputPath2);
		cmd1.add("-vcodec");
		cmd1.add("copy");
		cmd1.add("-acodec");
		cmd1.add("copy");
		cmd1.add("-vbsf");
		cmd1.add("h264_mp4toannexb");
		cmd1.add("/mnt/sdcard/input2.ts");
		
		final ProcessBuilder pb1 = new ProcessBuilder(cmd1);
		new ProcessRunnable(pb1).run();

		final List<String> cmd2 = new LinkedList<String>();
		cmd2.add(mFfmpegPath);
		cmd2.add("-i");
		cmd2.add("concat:/mnt/sdcard/input1.ts|/mnt/sdcard/input2.ts");
		
		cmd2.add("-vcodec"); 
		cmd2.add("copy");
		cmd2.add("-acodec");
		cmd2.add("copy");
		cmd2.add("-absf");
		cmd2.add("aac_adtstoasc");
		cmd2.add(outputPath);
		     
		final ProcessBuilder pb2 = new ProcessBuilder(cmd2);
		
		new ProcessRunnable(pb2).run();
		return new ProcessRunnable(pb2);
	}
	
	public class FFMPEGArg {
		String key;
		String value;
		
		public static final String ARG_VIDEOCODEC = "-vcodec";
		public static final String ARG_AUDIOCODEC = "-acodec";
		
		public static final String ARG_VIDEOBITSTREAMFILTER = "-vbsf";
		public static final String ARG_AUDIOBITSTREAMFILTER = "-absf";
		
		public static final String ARG_VIDEOFILTER = "-vf";
		public static final String ARG_AUDIOFILTER = "-af";
		
		public static final String ARG_VERBOSITY = "-v";
		public static final String ARG_FILE_INPUT = "-i";
		public static final String ARG_SIZE = "-s";
		public static final String ARG_FRAMERATE = "-r";
		public static final String ARG_FORMAT = "-f";
		public static final String ARG_BITRATE_VIDEO = "-b:v";
		
		public static final String ARG_BITRATE_AUDIO = "-b:a";
		public static final String ARG_CHANNELS_AUDIO = "-ac";
		public static final String ARG_FREQ_AUDIO = "-ar";
		public static final String ARG_VOLUME_AUDIO = "-vol";
		
		public static final String ARG_STARTTIME = "-ss";
		public static final String ARG_DURATION = "-t";
		
		public static final String ARG_DISABLE_VIDEO = "-vn";
		public static final String ARG_DISABLE_AUDIO = "-an";
	}
}
