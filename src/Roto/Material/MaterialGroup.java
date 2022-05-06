package Roto.Material;

public class MaterialGroup {
    private String name;
    private String text;
    //If this value double, 0 have to be the null
    private String customRebate;


    public MaterialGroup() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


    public String getCustomRebate() {
        if (customRebate == null) {
            customRebate = "";
        }
        return customRebate;
    }


    public void setCustomRebate(String customRebate) {
        this.customRebate = customRebate;
    }

}
