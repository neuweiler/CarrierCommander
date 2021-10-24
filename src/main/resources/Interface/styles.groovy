import com.jme3.math.Vector3f
import com.simsilica.lemur.*;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.component.*
import net.carriercommander.ui.hud.widgets.ToggleButton;

def gradient = TbtQuadBackgroundComponent.create(
        texture(name: "/com/simsilica/lemur/icons/bordered-gradient.png", generateMips: false),
        1, 1, 1, 126, 126,
        1f, false)

def bevel = TbtQuadBackgroundComponent.create(
        texture(name: "/com/simsilica/lemur/icons/bevel-quad.png", generateMips: false),
        0.125f, 8, 8, 119, 119,
        1f, false)

def border = TbtQuadBackgroundComponent.create(
        texture(name: "/com/simsilica/lemur/icons/border.png", generateMips: false),
        1, 1, 1, 6, 6,
        1f, false)
def border2 = TbtQuadBackgroundComponent.create(
        texture(name: "/com/simsilica/lemur/icons/border.png", generateMips: false),
        1, 2, 2, 6, 6,
        1f, false)

def doubleGradient = new QuadBackgroundComponent(color(0.5, 0.75, 0.85, 0.5))
doubleGradient.texture = texture(name: "/com/simsilica/lemur/icons/double-gradient-128.png", generateMips: false)

def backgroundColor = color(0.25, 0.5, 0.5, 0.5)

selector("cc") {
    fontSize = 14
}

selector("label", "cc") {
    insets = new Insets3f(2, 2, 0, 2)
    color = color(0.5, 0.75, 0.75, 0.85)
}

selector("container", "cc") {
    background = gradient.clone()
    background.setColor(backgroundColor)
}

selector("window", "cc") {
    background = gradient.clone()
    background.setColor(backgroundColor)
}

selector("slider", "cc") {
    background = gradient.clone()
    background.setColor(backgroundColor)
}

def pressedCommand = new Command<Button>() {
    void execute(Button source) {
        if (source.isPressed()) {
            source.move(1, -1, 0)
        } else {
            source.move(-1, 1, 0)
        }
    }
}

def toggleCommand = new Command<ToggleButton>() {
    void execute(ToggleButton source) {
            if (source.isSelected()) {
                source.setBackground(new QuadBackgroundComponent(color(1, 0.3, 0.3, 0.7)))
                source.move(1, -1, 0)
            } else {
                source.setBackground(new QuadBackgroundComponent(color(0, 0, 0, 0)))
                source.move(-1, 1, 0)
            }
        }
}

def repeatCommand = new Command<Button>() {
    private long startTime
    private long lastClick

    void execute(Button source) {
        if (source.isPressed() && source.isHighlightOn()) {
            long elapsedTime = System.currentTimeMillis() - startTime
            // After half a second pause, click 10 times a second
            if (elapsedTime > 500) {
                if (elapsedTime - lastClick > 100) {
                    source.click()
                    // Try to quantize the last click time to prevent drift
                    lastClick = (long) (((elapsedTime - 500) / 125) * 125 + 500)
                }
            }
        } else {
            startTime = System.currentTimeMillis()
            lastClick = 0
        }
    }
}

def stdButtonCommands = [
        (ButtonAction.Down): [pressedCommand],
        (ButtonAction.Up)  : [pressedCommand]
]

def toggleButtonCommands = [
        (ButtonAction.Down): [toggleCommand]
]

def repeatButtonCommands = [
        (ButtonAction.Hover): [repeatCommand]
]

selector("title", "cc") {
    color = color(0.8, 0.9, 1, 0.85f)
    highlightColor = color(1, 0.8, 1, 0.85f)
    shadowColor = color(0, 0, 0, 0.75f)
    shadowOffset = new Vector3f(2, -2, -1)
    background = new QuadBackgroundComponent(color(0.5, 0.75, 0.85, 0.5))
    background.texture = texture(name: "/com/simsilica/lemur/icons/double-gradient-128.png", generateMips: false)
    insets = new Insets3f(2, 2, 2, 2)

    buttonCommands = stdButtonCommands
}


selector("button", "cc") {
    background = gradient.clone()
    color = color(0.8, 0.9, 1, 0.85f)
    background.setColor(color(0, 0.75, 0.75, 0.5))
    insets = new Insets3f(2, 2, 2, 2)

    buttonCommands = stdButtonCommands
}

selector("imageButton", "cc") {
    insets = new Insets3f(0, 0, 0, 0)
    buttonCommands = stdButtonCommands
}

selector("toggleButton", "cc") {
    insets = new Insets3f(0, 0, 0, 0)
    buttonCommands = toggleButtonCommands
}

selector("toggleImageButton", "cc") {
    insets = new Insets3f(0, 0, 0, 0)
    buttonCommands = toggleButtonCommands
}

selector("repeatButton", "cc") {
    insets = new Insets3f(0, 0, 0, 0)
    buttonCommands = repeatButtonCommands
}

selector("slider", "cc") {
    insets = new Insets3f(1, 3, 1, 2)
}

selector("slider", "button", "cc") {
    background = doubleGradient.clone()
    background.setColor(color(0.5, 0.75, 0.75, 0.5))
    insets = new Insets3f(0, 0, 0, 0)
}

selector("slider.thumb.button", "cc") {
    text = "[]"
    color = color(0.6, 0.8, 0.8, 0.85)
}

selector("slider.left.button", "cc") {
    text = "-"
    background = doubleGradient.clone()
    background.setColor(color(0.5, 0.75, 0.75, 0.5))
    background.setMargin(5, 0)
    color = color(0.6, 0.8, 0.8, 0.85)

    buttonCommands = repeatButtonCommands
}

selector("slider.right.button", "cc") {
    text = "+"
    background = doubleGradient.clone()
    background.setColor(color(0.5, 0.75, 0.75, 0.5))
    background.setMargin(4, 0)
    color = color(0.6, 0.8, 0.8, 0.85)

    buttonCommands = repeatButtonCommands
}

selector("slider.up.button", "cc") {
    buttonCommands = repeatButtonCommands
}

selector("slider.down.button", "cc") {
    buttonCommands = repeatButtonCommands
}

selector("checkbox", "cc") {
    def on = new IconComponent("/com/simsilica/lemur/icons/Glass-check-on.png", 1f,
            0, 0, 1f, false)
    on.setColor(color(0.5, 0.9, 0.9, 0.9))
    on.setMargin(5, 0)
    def off = new IconComponent("/com/simsilica/lemur/icons/Glass-check-off.png", 1f,
            0, 0, 1f, false)
    off.setColor(color(0.6, 0.8, 0.8, 0.8))
    off.setMargin(5, 0)

    onView = on
    offView = off

    color = color(0.8, 0.9, 1, 0.85f)
}

selector("rollup", "cc") {
    background = gradient.clone()
    background.setColor(backgroundColor)
}

selector("tabbedPanel", "cc") {
    activationColor = color(0.8, 0.9, 1, 0.85f)
}

selector("tabbedPanel.container", "cc") {
    background = null
}

selector("tab.button", "cc") {
    background = gradient.clone()
    background.setColor(backgroundColor)
    color = color(0.4, 0.45, 0.5, 0.85f)
    insets = new Insets3f(4, 2, 0, 2)

    buttonCommands = stdButtonCommands
}
