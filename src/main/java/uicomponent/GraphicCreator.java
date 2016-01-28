package uicomponent;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * @author 14r4113 on 2016/01/13.
 */
public class GraphicCreator {
	private static final GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
	public static Glyph createEffectIcon(FontAwesome.Glyph g){
		return fontAwesome.create(g).useGradientEffect().useHoverEffect();
	}
	public static Glyph createIcon(FontAwesome.Glyph g){
		return fontAwesome.create(g);
	}
}
