// Copyright (c) 2019-present, iQIYI, Inc. All rights reserved.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

// Created by caikelun on 2019-03-07.
package com.linkb.lib.xcrash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;


/**
 * NOTICE:
 *
 * We are trying to keep this Java wrapper class as simple and clear as possible.
 * Because in many cases, the native part of xCrash will be used independently.
 */
class NativeCrashHandler {

    private static final NativeCrashHandler instance = new NativeCrashHandler();

    private ICrashCallback callback = null;

    private NativeCrashHandler() {
    }

    static NativeCrashHandler getInstance() {
        return instance;
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    int initialize(Context ctx, String appVersion, String logDir, int logCountMax,
                   int logcatSystemLines, int logcatEventsLines, int logcatMainLines,
                   boolean dumpMap, boolean dumpFds, boolean dumpAllThreads,
                   int dumpAllThreadsCountMax, String[] dumpAllThreadsWhiteList,
                   ICrashCallback callback) {
        //load lib
        try {
            System.loadLibrary("xcrash");
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                //for some unusual Android version
                System.load(ctx.getFilesDir().getParent() + "/lib/libxcrash.so");
            } catch (Throwable e2) {
                e2.printStackTrace();
                return Errno.LOAD_LIBRARY_FAILED;
            }
        }

        this.callback = callback;

        //init
        try {
            return initEx(ctx,
                    true,
                    appVersion,
                    logDir,
                    Util.logPrefix,
                    Util.nativeLogSuffix,
                    logCountMax,
                    logcatSystemLines,
                    logcatEventsLines,
                    logcatMainLines,
                    dumpMap,
                    dumpFds,
                    dumpAllThreads,
                    dumpAllThreadsCountMax,
                    dumpAllThreadsWhiteList,
                    "xcrash/NativeCrashHandler",
                    "callback");
        } catch (Throwable e) {
            e.printStackTrace();
            return Errno.INIT_LIBRARY_FAILED;
        }
    }

    void testNativeCrash(boolean runInNewThread) {
        NativeCrashHandler.test(runInNewThread ? 1 : 0);
    }

    // do NOT obfuscate this method
    @SuppressWarnings("unused")
    private static void callback(String logPath, String emergency) {

        if (!TextUtils.isEmpty(logPath)) {
            TombstoneManager.appendSection(logPath, "memory info", Util.getMemoryInfo());
        }

        ICrashCallback callback = NativeCrashHandler.getInstance().callback;
        if (callback != null) {
            try {
                callback.onCrash(logPath, emergency);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unused")
    private static native int init(Context ctx);

    private static native int initEx(
            Context ctx,
            boolean restoreSignalHandler,
            String appVersion,
            String logDir,
            String logPrefix,
            String logSuffix,
            int logCountMax,
            int logcatSystemLines,
            int logcatEventsLines,
            int logcatMainLines,
            boolean dumpMap,
            boolean dumpFds,
            boolean dumpAllThreads,
            int dumpAllThreadsCountMax,
            String[] dumpAllThreadsWhiteList,
            String callbackClass,
            String callbackMethod);

    private static native void test(int runInNewThread);
}
