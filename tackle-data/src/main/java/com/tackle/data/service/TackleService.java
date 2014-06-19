package com.tackle.data.service;

import android.util.Log;

import com.tackle.data.TackleData;
import com.tackle.data.model.Category;

import rx.Observable;
import rx.Subscriber;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleService {

    public Observable<Category> createCategory(final Category category){
        return Observable.create(new Observable.OnSubscribe<Category>() {
            @Override
            public void call(Subscriber<? super Category> subscriber) {
                try {
                    boolean saved = category.save();
                    if (saved){
                        subscriber.onNext(category);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new Exception("Error saving category"));
                    }
                } catch (Exception e){
                    Log.e(TackleData.TAG, e.toString());
                }
            }
        });
    }
}
