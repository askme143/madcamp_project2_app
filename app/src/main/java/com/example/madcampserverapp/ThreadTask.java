package com.example.madcampserverapp;

public abstract class ThreadTask<T1,T2> implements Runnable {
    T1 mArgument;   // argument
    T2 mResult;     // result

    // Execute
    final public void execute(final T1 arg) {
        // Store the argument
        mArgument = arg;

        // Call onPreExecute
        onPreExecute();

        // Begin thread work
        Thread thread = new Thread(this);
        thread.start();

        // Wait for the thread work
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            onPostExecute(null);
            return;
        }

        // Call onPostExecute
        onPostExecute(mResult);
    }

    @Override
    public void run() {
        mResult = doInBackground(mArgument);
    }

    // onPreExecute
    protected abstract void onPreExecute();

    // doInBackground
    protected abstract T2 doInBackground(T1 arg);

    // onPostExecute
    protected abstract void onPostExecute(T2 result);
}