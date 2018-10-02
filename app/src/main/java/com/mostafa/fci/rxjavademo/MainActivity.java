package com.mostafa.fci.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "RX_JAVA";


    //private Disposable disposable;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Observable<String> mObservable = getObservable();

        Observer<String> mObserver = getObserver();
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);

        // do filter on data

        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                })
                .subscribe(mObserver);


        DisposableObserver<String> mDispObserver = getDisposableObserver();
        DisposableObserver<String> mAllCapsDispObserver = getAllCapsDisposableObserver();

        mCompositeDisposable.add(
                mObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("b");
                            }
                        })
                        .subscribeWith(mDispObserver)
        );

        mCompositeDisposable.add(
                mObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("c");
                            }
                        })
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(String s) throws Exception {
                                return s.toUpperCase();
                            }
                        })
                        .subscribeWith(mAllCapsDispObserver)
        );
        */

        mCompositeDisposable.add(
                getNoteObservable().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Note, Note>() {

                            @Override
                            public Note apply(Note note) throws Exception {
                                note.setNote(note.getNote().toUpperCase());
                                return note;
                            }
                        })
                        .subscribeWith(getNotesObserver())
        );

    }



    private Observable<Note> getNoteObservable(){
        final List<Note> notes = prepareNotes();
        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {

                for (Note note:notes){
                    if (!emitter.isDisposed())
                        emitter.onNext(note);
                }

                if (!emitter.isDisposed())
                    emitter.onComplete();

            }
        });
    }

    private DisposableObserver<Note> getNotesObserver(){
        return new DisposableObserver<Note>() {
            @Override
            public void onNext(Note note) {
                Log.v(TAG, "note id: " + note.getId() + ", note: " + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.v(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.v(TAG, "All items are emitted!");
            }
        };
    }

    private List<Note> prepareNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1, "buy tooth paste!"));
        notes.add(new Note(2, "call brother!"));
        notes.add(new Note(3, "watch narcos tonight!"));
        notes.add(new Note(4, "pay power bill!"));

        return notes;
    }

    private Observable<String> getObservable() {
        // return Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");
        return Observable.fromArray(
                "Ant", "Ape",
                "Bat", "Bee", "Bear", "Butterfly",
                "Cat", "Crab", "Cod",
                "Dog", "Dove",
                "Fox", "Frog");

    }

    private DisposableObserver<String> getDisposableObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.v(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.v(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.v(TAG, "All items are emitted!");
            }
        };
    }

    private DisposableObserver<String> getAllCapsDisposableObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.v(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.v(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.v(TAG, "All items are emitted!");
            }
        };
    }


    private Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.v(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.v(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.v(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.v(TAG, "All items are emitted!");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mCompositeDisposable.clear();
        // don't send events once the activity is destroyed
        //disposable.dispose();
    }

    // end Main Activity Class
}
