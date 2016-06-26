package io.github.kolacbb.translate.flux.actions;

/**
 * Created by Kola on 2016/6/5.
 */
public interface TranslateActions {
    String TRANSLATE_CREATE = "translate_create";

    String ACTION_TRANSLATION_NET_ERROR = "action_translation_net_error";
    String ACTION_TRANSLATION_INIT_VIEW = "action_translation_init_view";
    String ACTION_TRANSLATION_FINISH = "action_translation_finish";
    String ACTION_TRANSLATION_LOADING = "action_translation_loading";

    String ACTION_PHRASEBOOK_INIT = "actioin_phrasebook_init";

    String ACTION_SMS_INIT = "action_sms_init";
    String ACTION_SMS_ERROR = "action_sms_error";
    String KEY_SMS_LIST = "key_sms_list";

    String KEY_TRANSLATION_ANSWER = "key_translation_answer";
    String KEY_TRANSLATION_HISTORY = "key_translation_history";
    String KEY_PHRASEBOOK_FAVORITE = "key_phrasebook_favorite";
}
