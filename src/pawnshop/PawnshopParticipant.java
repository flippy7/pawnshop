package pawnshop;

/** Спільна ідентифікація учасників. */
public abstract class PawnshopParticipant {
    private String id;
    private String name;
    protected PawnshopParticipant(String id, String name) {
        if (id == null || id.isEmpty() || id.codePoints().anyMatch(c -> Character.isWhitespace(c) || Character.isSpaceChar(c)))
            throw new IllegalArgumentException("Некоректний ідентифікатор");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Ім’я не задано");
        this.id = id; this.name = name;
    }
    public final String getId() { return id; }
    public abstract String getRole();
}
