package io.github.tr100000.researcher.api.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class IndentedTextHolder implements Consumer<Text> {
    private static final int DEFAULT_INDENT = 2;

    private final List<MutableText> text = new ObjectArrayList<>();
    private int indent = 0;

    public int push(int amount) {
        indent += amount;
        return indent;
    }

    public int pop(int amount) {
        indent -= amount;
        return indent;
    }

    public int push() {
        return push(DEFAULT_INDENT);
    }

    public int pop() {
        return pop(DEFAULT_INDENT);
    }

    public void accept(Text text) {
        this.text.add(getIndentText().append(text));
    }

    private MutableText getIndentText() {
        return Text.literal(" ".repeat(indent));
    }

    public List<MutableText> getText() {
        return text;
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }
}
