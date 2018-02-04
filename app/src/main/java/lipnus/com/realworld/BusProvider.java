package lipnus.com.realworld;

import com.squareup.otto.Bus;

/**
 * Created by Sunpil on 2018-02-01.
 * Otto를 관리해주는 Singleton개념의 BusProvider 클래스
 *
 */

public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
