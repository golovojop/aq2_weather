package j.s.yarlykov.data.provider;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.History;
import j.s.yarlykov.util.Utils;

public class HistoryProvider {
    private HistoryProvider() {}

    private static List<History> listHistory = new ArrayList<>();

    /**
     * Генерит прогноз за последние daysAgo дней.
     *
     * @isNeedNew - определяет нужно ли генерить новый массив данных
     * или вернуть имеющийся. Нужен для случаев восстановления активити
     * с тем же городом.
     */
    public static List<History> build(Context context, int days, boolean isNeedNew) {

        if(!isNeedNew) return listHistory;

        listHistory.clear();
        Date today = new Date();
        TypedArray images = context.getResources().obtainTypedArray(R.array.historyLogos);

        for(int i = 1; i <= days; i++) {
            listHistory.add(new History(Utils.daysAgo(today, i),
                    String.format("%s\u2103 ~ %s\u2103", tDay(), tNight()),
                    images.getResourceId(inRange(0, images.length() - 1), 0)));
        }

        images.recycle();
        return listHistory;
    }

    // Прогноз ещё на один день
    public static void oneMoreDay(Context context) {
        TypedArray images = context.getResources().obtainTypedArray(R.array.historyLogos);
        listHistory.add(new History(Utils.daysAgo(new Date(), listHistory.size() + 1),
                String.format("%s\u2103 ~ %s\u2103", tDay(), tNight()),
                images.getResourceId(inRange(0, images.length() - 1), 0)));
    }

    // Дневная температура
    private static int tDay() {
        return inRange(15, 10);
    }

    // Ночная температура
    private static int tNight() {
        return inRange(8, 6);
    }

    // Генератор чисел в диапазоне min ~ (min + range)
    private static int inRange(int min, int range) {
        return min + (new Random()).nextInt(range);
    }
}