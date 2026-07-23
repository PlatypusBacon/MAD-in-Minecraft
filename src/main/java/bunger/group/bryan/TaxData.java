package bunger.group.bryan;

import java.util.Map;

public class TaxData {

    public Map<String, Integer> requiredItems;
    public long dueDay;
    public boolean paid;

    public TaxData(Map<String, Integer> requiredItems, long dueDay) {
        this.requiredItems = requiredItems;
        this.dueDay = dueDay;
        this.paid = false;
    }
}