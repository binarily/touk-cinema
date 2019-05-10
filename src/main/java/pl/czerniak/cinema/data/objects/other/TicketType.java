package pl.czerniak.cinema.data.objects.other;

public enum TicketType {
    ADULT, STUDENT, CHILD;
    private Double price;

    static {
        ADULT.price = 25.0;
        STUDENT.price = 18.0;
        CHILD.price = 12.5;
    }

    public Double getPrice(){
        return this.price;
    }
}
