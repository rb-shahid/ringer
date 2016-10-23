/*
 * Copyright (C) 2012 Jacquet Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * musicg api in Google Code: http://code.google.com/p/musicg/
 * Android Application in Google Play: https://play.google.com/store/apps/details?id=com.whistleapp
 * 
 */

package info.ss12.audioalertsystem;
import android.media.AudioRecord;

public class RecorderThread extends Thread
{

    private int audioEncoding;
    private AudioRecord audioRecord;
    byte buffer[];
    private int channelConfiguration;
    private int frameByteSize;
    private boolean isRecording;
    private int sampleRate;

    public RecorderThread()
    {
        channelConfiguration = 16;
        audioEncoding = 2;
        sampleRate = 44100;
        frameByteSize = 2048;
        int i = AudioRecord.getMinBufferSize(sampleRate, channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(1, sampleRate, channelConfiguration, audioEncoding, i);
        buffer = new byte[frameByteSize];
    }

    public AudioRecord getAudioRecord()
    {
        return audioRecord;
    }

    public byte[] getFrameBytes()
    {
        audioRecord.read(buffer, 0, frameByteSize);
        int j = 0;
        int i = 0;
        do
        {
            if (i >= frameByteSize)
            {
                if ((float)(j / frameByteSize / 2) < 30F)
                {
                    return null;
                } else
                {
                    return buffer;
                }
            }
            j += Math.abs((short)(buffer[i] | buffer[i + 1] << 8));
            i += 2;
        } while (true);
    }

    public boolean isRecording()
    {
        return isAlive() && isRecording;
    }

    public void run()
    {
        startRecording();
        do
        {
            if (!isRecording())
            {
                return;
            }
            if (!isAlive())
            {
                startRecording();
            }
            try
            {
                Thread.sleep(5L);
            }
            catch (InterruptedException interruptedexception)
            {
                isRecording = false;
            }
        } while (true);
    }

    public void startRecording()
    {
        try
        {
            audioRecord.startRecording();
            isRecording = true;
            return;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void stopRecording()
    {
        try
        {
            audioRecord.stop();
            isRecording = false;
            return;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}