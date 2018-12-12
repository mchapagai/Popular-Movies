package com.example.mchapagai.view_model.impl;

import com.example.mchapagai.model.AccountDetails;
import com.example.mchapagai.model.AuthSession;
import com.example.mchapagai.model.AuthToken;
import com.example.mchapagai.service.LoginService;
import com.example.mchapagai.utils.RxUtils;
import com.example.mchapagai.view_model.LoginViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Single;

public class LoginViewModelImpl implements LoginViewModel {

    private Provider<LoginService> movieService;

    @Inject
    public LoginViewModelImpl(Provider<LoginService> movieService) {
        this.movieService = movieService;
    }

    @Override
    public Single<AuthToken> getAuthRequestToken() {
        return movieService.get().getRequestToken().compose(RxUtils.applySingleSchedulers());
    }

    @Override
    public Single<AuthToken> getRequestAuthenticated(String requestToken, String username, String password) {
        return movieService.get().getRequestAuthenticated(requestToken, username, password).compose(RxUtils.applySingleSchedulers());
    }

    @Override
    public Single<AuthSession> getSessionID(String requestToken) {
        return movieService.get().getSessionID(requestToken).compose(RxUtils.applySingleSchedulers());
    }

    @Override
    public Single<AccountDetails> getAccountDetails(String sessionId) {
        return movieService.get().getAccountDetails(sessionId).compose(RxUtils.applySingleSchedulers());
    }
}
