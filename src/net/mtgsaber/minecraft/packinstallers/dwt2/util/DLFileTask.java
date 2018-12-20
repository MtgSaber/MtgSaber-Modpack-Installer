package net.mtgsaber.minecraft.packinstallers.dwt2.util;

import javafx.util.Pair;
import net.mtgsaber.lib.Clock;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Vector;

/**
 * Author: Andrew Arnold (12/18/2018)
 */
public class DLFileTask implements Runnable {
    public volatile boolean
            isBusy, isDone,
            isMalformedURLException,
            isFileNotFoundException,
            isIOException
            ;

    public final String URL;
    public final File TARG;
    public final Vector<Exception> EXCEPTIONS;

    public DLFileTask(String URL, File TARG) {
        this.URL = URL;
        this.TARG = TARG;
        this.EXCEPTIONS = new Vector<>();
    }

    @Override
    public void run() {
        isBusy = true;
        /*
         * Original Code Here:
         * https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
         */
        try (
                InputStream is = new URL(URL).openStream();
                ReadableByteChannel rbc = Channels.newChannel(is);
                FileOutputStream fos = new FileOutputStream(TARG)
        ) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (MalformedURLException ex) {
            isMalformedURLException = true;
            EXCEPTIONS.add(ex);
        } catch (FileNotFoundException ex) {
            isFileNotFoundException = true;
            EXCEPTIONS.add(ex);
        } catch (IOException ex) {
            isIOException = true;
            EXCEPTIONS.add(ex);
        } finally {
            isBusy = false;
            isDone = true;
        }
    }

    public static class DLFileTaskMaster implements Clock.Tickable {
        private static volatile int instanceCount;

        private final Vector<Pair<Thread, DLFileTask>> TASKS;

        public volatile double prog;
        public volatile long cap;
        public volatile boolean busy, done;
        public volatile Vector<DLFileTask> completed;

        public DLFileTaskMaster(DLFileTask... TASKS) {
            instanceCount++;
            this.TASKS = new Vector<>();
            for (int i=0; i<TASKS.length; i++) {
                this.TASKS.add(new Pair<>(
                        new Thread(
                                TASKS[i],
                                "DLFileTaskMaster_"
                                        + instanceCount
                                        + "-Task_"
                                        + i
                        ),
                        TASKS[i]
                ));
            }
            completed = new Vector<>(TASKS.length);
            cap = TASKS.length;
        }

        public void start() {
            busy = true;
            for (Pair<Thread, DLFileTask> pair : TASKS)
                pair.getKey().start();
        }

        @Override
        public void tick() {
            if (!done) {
                TASKS.iterator().forEachRemaining(threadDLFileTaskPair -> {
                    if (threadDLFileTaskPair.getValue().isDone
                            && threadDLFileTaskPair.getKey().getState().equals(Thread.State.TERMINATED)
                            && !completed.contains(threadDLFileTaskPair.getValue())
                            )
                        completed.add(threadDLFileTaskPair.getValue());
                });
                prog = completed.size()/cap;
                done = prog == 1;
            }
            if (done) {
                busy = false;
            }
        }

        @Override
        public void run() {

        }
    }
}
