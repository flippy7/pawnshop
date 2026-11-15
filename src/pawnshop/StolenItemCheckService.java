package pawnshop;

import java.util.HashSet;
import java.util.Set;
/** Локальний пошук і консольна імітація перевірки. */
public class StolenItemCheckService implements IStolenItemCheck {
    private Set<String> stolenItemIds;
    public StolenItemCheckService(Set<String> stolenItemIds) {
        if (stolenItemIds == null) throw new IllegalArgumentException("Набір перевірки не задано");
        this.stolenItemIds = new HashSet<>(stolenItemIds);
    }
    @Override public boolean isStolen(CollateralItem item) {
        if (item == null) throw new IllegalArgumentException("Річ не задано");
        boolean result = stolenItemIds.contains(item.getId());
        System.out.println("Локальна перевірка речі " + item.getId() + ": викрадення " + (result ? "виявлено" : "не виявлено"));
        return result;
    }
}
