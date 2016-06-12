package io.github.kolacbb.translate.flux.actions;

/**
 * 用于管理所有的ActionCreator
 * Created by Kola on 2016/6/11.
 */
public class ActionCreatorManager {
    private TranslateActionCreator translateActionCreator;

    public ActionCreatorManager(TranslateActionCreator translateActionCreator){
        this.translateActionCreator = translateActionCreator;
    }

    public TranslateActionCreator getTranslateActionCreator() {
        return translateActionCreator;
    }
}
