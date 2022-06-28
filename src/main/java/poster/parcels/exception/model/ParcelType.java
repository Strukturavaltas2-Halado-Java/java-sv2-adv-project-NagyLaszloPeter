package poster.parcels.exception.model;

public enum ParcelType {

    OVERSIZE(10), HUGE(8), NORMAL(5), MINI(2);

    private final int capacity;


    ParcelType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}