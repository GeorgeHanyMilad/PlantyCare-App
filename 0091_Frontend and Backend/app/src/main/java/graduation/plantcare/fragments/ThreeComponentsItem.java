package graduation.plantcare.fragments;
public class ThreeComponentsItem {
    private final int iconResId;
    private final String name;
    private final String description;

    public ThreeComponentsItem(int iconResId, String name, String description) {
        this.iconResId = iconResId;
        this.name = name;
        this.description = description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
