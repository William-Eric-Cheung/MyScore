package com.vancior.myscore.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.vancior.myscore.audio.bean.Chord;
import com.vancior.myscore.audio.bean.Note;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by H on 2016/12/1.
 */

public class MatchThread extends Thread {

    private static String TAG = "TestThread";

    private static double[] temperament = new double[]{
            32.70,   34.65,   36.71,   38.89,   41.20,   43.65,   46.25,   49.00,   51.91,   55.00,   58.27,   61.74,
            65.41,   69.30,   73.42,   77.78,   82.41,   87.31,   92.50,   98.00,   103.83,  110.00,  116.54,  123.47,
            130.18,  138.59,  146.83,  155.56,  164.81,  174.61,  185.00,  196.00,  207.65,  220.00,  233.08,  246.94,
            261.63,  277.18,  293.66,  311.13,  329.63,  349.23,  369.99,  392.00,  415.30,  440.00,  466.16,  493.88,
            523.25,  554.37,  587.33,  622.25,  659.25,  698.46,  739.99,  783.99,  830.61,  880.00,  932.33,  987.77,
            1046.50, 1108.73, 1174.66, 1244.51, 1318.51, 1396.91, 1479.98, 1567.98, 1661.22, 1760.00, 1864.66, 1975.53,
            2093.00, 2217.46, 2349.32, 2489.02, 2637.02, 2793.83, 2959.96, 3135.96, 3322.44, 3520.00, 3729.31, 3951.07};

    private static String[] toneString = new String[]{
//            "C1", "C1#", "D1", "D1#", "E2", "F2", "F2#", "G2", "G2#", "A2", "A2#", "B2",
            "C2", "C2#", "D2", "D2#", "E2", "F2", "F2#", "G2", "G2#", "A2", "A2#", "B2",
            "C3", "C3#", "D3", "D3#", "E3", "F3", "F3#", "G3", "G3#", "A3", "A3#", "B3",
            "C4", "C4#", "D4", "D4#", "E4", "F4", "F4#", "G4", "G4#", "A4", "A4#", "B4",
            "C5", "C5#", "D5", "D5#", "E5", "F5", "F5#", "G5", "G5#", "A5", "A5#", "B5",
            "C6", "C6#", "D6", "D6#", "E6", "F6", "F6#", "G6", "G6#", "A6", "A6#", "B6",
            "C7", "C7#", "D7", "D7#", "E7", "F7", "F7#", "G7", "G7#", "A7", "A7#", "B7",
            "C8", "C8#", "D8", "D8#", "E8", "F8", "F8#", "G8", "G8#", "A8", "A8#", "B8",
    };

    private static Map<String, Integer> toneIndex = new HashMap<>();

    static {
        int itr = 0;
        for (String str : toneString) {
            toneIndex.put(str, itr++);
        }
    }

    private Handler handler;
    private boolean isRunning;
    private double step;
    private static int[] mSampleRates = new int[]{8000, 11025, 22050, 44100};
    private double[] spectrumArray;
    private List<Chord> chordList;
    private Chord currentChord;
    private Chord nextChord;
    private int current = 0;
    private int chordsLength;
    private boolean newPage;
    private float sensitivity;

    public MatchThread(Handler handler, List<Chord> chordList) {
        this.handler = handler;
        this.chordList = chordList;
        chordsLength = this.chordList.size();
        newPage = false;
        this.sensitivity = 50.0f;
    }

    public void setRunning(boolean b) {
        this.isRunning = b;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        int sampleRate = 22050;
        int bufferSize = 8192;

        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        record.startRecording();

        byte[] buffer = new byte[bufferSize];

        step = (double) sampleRate / bufferSize;
        int length;

        while (isRunning) {
            int byteRead = record.read(buffer, 0, bufferSize);

            if (byteRead > 0) {

                Spectrum spectrum = new Spectrum(buffer, sampleRate);
                spectrumArray = spectrum.getSpectrum();

                length = spectrumArray.length;

                if (current < chordsLength && testNotation()) {
                    if (newPage) {
                        String send = "OK";
                        Message message = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("Note", send);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        Log.d("Send", "succeed");

                        newPage = false;
                    }
                } else if (current >= chordsLength) {
                    isRunning = false;
                    Log.d(TAG, "run: end!");
                }

            }

        }

        record.stop();
        record.release();
    }

    private boolean testNotation() {
        boolean result = true;
        int left, right;
        List<Note> noteList;

        noteList = chordList.get(current).getChord();

        for (Note j : noteList) {
            String temp = "";
            temp += j.getPitchStep();
            temp += j.getPitchOctave();
            //use alter to get the right temperament
            left = (int) (temperament[toneIndex.get(temp) + j.getAlter()] / step);
            right = left + 1;
            int left2 = left - 1;
//            if (spectrumArray[left2] < 50.0 && spectrumArray[left] < 50.0 && spectrumArray[right] < 50.0) {
//                result = false;
//            } else {
//                String msg = (left-2)*step + ": " + spectrumArray[left-2] + " " + (left-1)*step + ": " + spectrumArray[left-1] +
//                        " " + left*step + ": " + spectrumArray[left] + " " + (left+1)*step + ": " + spectrumArray[left+1] +
//                        " " + (left+2)*step + ": " + spectrumArray[left+2];
//                Log.d(TAG, "testNotation: " + msg);
//            }
//            if (!result)
//                break;

            if (spectrumArray[left] < sensitivity && spectrumArray[right] < sensitivity) {
                result = false;
            } else {
                String msg = spectrumArray[left] + " " + spectrumArray[left+1];
                Log.d(TAG, "testNotation: " + msg);
            }
            if (!result)
                break;

            Log.d(TAG, "testNotation: " + left + " " + right);
        }
        if (result) {
            Log.d(TAG, "testNotation: " + chordList.get(current).toString());
            if (chordList.get(current).getNewPage())
                newPage = true;
            current++;
        }
        return result;
    }

    /**
     * Quadratic Interpolation of Peak Location
     *
     * <p>Provides a more accurate value for the peak based on the
     * best fit parabolic function.
     *
     * <p>α = spectrum[max-1]
     * <br>β = spectrum[max]
     * <br>γ = spectrum[max+1]
     *
     * <p>p = 0.5[(α - γ) / (α - 2β + γ)] = peak offset
     *
     * <p>k = max + p = interpolated peak location
     *
     * <p>Courtesy: <a href="https://ccrma.stanford.edu/~jos/sasp/Quadratic_Interpolation_Spectral_Peaks.html">
     * information source</a>.
     *
     * @param index The estimated peak value to base a quadratic interpolation on.
     * @return Float value that represents a more accurate peak index in a spectrum.
     */
    private double quadraticPeak(int index) {
        double alpha, beta, gamma, p, k;

        alpha = spectrumArray[index-1];
        beta = spectrumArray[index];
        gamma = spectrumArray[index+1];

        p = 0.5f * ((alpha - gamma) / (alpha - 2*beta + gamma));

        k = index + p;

        return k;
    }

}