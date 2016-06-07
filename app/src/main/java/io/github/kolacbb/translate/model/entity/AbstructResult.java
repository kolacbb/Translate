package io.github.kolacbb.translate.model.entity;

/**
 * Created by Kola on 2016/6/7.
 */
public abstract class AbstructResult {
    public Result getResult(Object obj) {
        Result result = null;

        if (obj instanceof YouDaoResult) {
            YouDaoResult youDaoResult = (YouDaoResult) obj;
            result = new Result();
            result.setQuery(youDaoResult.getQuery());
            result.setTranslation(youDaoResult.getTranslation().get(0));
            if (youDaoResult.getBasic() != null) {
                YouDaoResult.Basic basic = youDaoResult.getBasic();
                result.setUk_phonetic(basic.getUkPhonetic());
                result.setUs_phonetic(basic.getUsPhonetic());
                StringBuilder sb = new StringBuilder();
                for (String string : basic.getExplains()) {
                    sb.append(string);
                    sb.append('\n');
                }
                result.setBasic(sb.toString());
            }
        }
        return result;
    }
}
