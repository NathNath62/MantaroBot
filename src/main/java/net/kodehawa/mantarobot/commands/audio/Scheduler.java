package net.kodehawa.mantarobot.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Scheduler extends AudioEventAdapter {
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;

	public Scheduler(GuildMessageReceivedEvent event, AudioPlayer player) {
		GuildMessageReceivedEvent event1 = event;
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
			/*event.getChannel().sendMessage("\uD83D\uDCE3 Now playing ->``" + getPlayer().getPlayingTrack().getInfo().title
				+ " (" + Utils.getDurationMinutes(getPlayer().getPlayingTrack().getInfo().length) + ")``").queue(); //
				TODO: java.lang.NullPointerException: null
				at net.kodehawa.mantarobot.commands.audio.Scheduler.onTrackEnd(Scheduler.java:29) ~[main/:na]
				at com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter.onEvent(AudioEventAdapter.java:70) ~[lavaplayer-1.1.47.jar:na]
			*/
		}
	}

	public AudioPlayer getPlayer() {
		return player;
	}

	public BlockingQueue<AudioTrack> getQueue() {
		return queue;
	}

	public String getQueueList() {
		StringBuilder sb = new StringBuilder();
		int n = 1;
		for (AudioTrack audioTrack : queue) {
			long aDuration = audioTrack.getDuration();
			String duration = String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(aDuration),
				TimeUnit.MILLISECONDS.toSeconds(aDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(aDuration))
			);

			sb.append("[").append(n)
				.append("] ")
				.append(audioTrack.getInfo().title)
				.append(" **(")
				.append(duration)
				.append(")**")
				.append("\n"
				);
			n++;
		}
		return sb.toString();
	}

	public int getQueueSize() {
		return queue.size();
	}

	public void nextTrack() {
		player.startTrack(queue.poll(), false);
	}

	public void queue(AudioTrack track) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}
}
