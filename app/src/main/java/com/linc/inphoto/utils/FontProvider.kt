package com.linc.inphoto.utils

import android.content.res.Resources
import android.graphics.Typeface
import android.text.TextUtils
import java.util.*

/**
 * extracting Typeface from Assets is a heavy operation,
 * we want to make sure that we cache the typefaces for reuse
 */
class FontProvider(private val resources: Resources) {
    private val typefaces: MutableMap<String?, Typeface?>
    private val fontNameToTypefaceFile: Map<String?, String>
    val fontNames: List<String?>

    /**
     * @param typefaceName must be one of the font names provided from [FontProvider.getFontNames]
     * @return the Typeface associated with `typefaceName`, or [Typeface.DEFAULT] otherwise
     */
    fun getTypeface(typefaceName: String?): Typeface? {
        return if (TextUtils.isEmpty(typefaceName)) {
            Typeface.DEFAULT
        } else {
            if (typefaces[typefaceName] == null) {
                typefaces[typefaceName] = Typeface.createFromAsset(
                    resources.assets,
                    "fonts/" + fontNameToTypefaceFile[typefaceName]
                )
            }
            typefaces[typefaceName]
        }
    }

    /**
     * @return Default Font Name - **Helvetica**
     */
    fun getDefaultFontName(): String {
        return DEFAULT_FONT_NAME
    }

    companion object {
        private const val DEFAULT_FONT_NAME = "Helvetica"
    }

    init {
        typefaces = HashMap()

        // populate fonts
        fontNameToTypefaceFile = HashMap()
        //        fontNameToTypefaceFile.put("Arial", "Arial.ttf");
//        fontNameToTypefaceFile.put("Eutemia", "Eutemia.ttf");
//        fontNameToTypefaceFile.put("GREENPIL", "GREENPIL.ttf");
//        fontNameToTypefaceFile.put("Grinched", "Grinched.ttf");
//        fontNameToTypefaceFile.put("Helvetica", "Helvetica.ttf");
//        fontNameToTypefaceFile.put("Libertango", "Libertango.ttf");
//        fontNameToTypefaceFile.put("Metal Macabre", "MetalMacabre.ttf");
//        fontNameToTypefaceFile.put("Parry Hotter", "ParryHotter.ttf");
//        fontNameToTypefaceFile.put("SCRIPTIN", "SCRIPTIN.ttf");
//        fontNameToTypefaceFile.put("The Godfather v2", "TheGodfather_v2.ttf");
//        fontNameToTypefaceFile.put("Aka Dora", "akaDora.ttf");
//        fontNameToTypefaceFile.put("Waltograph", "waltograph42.ttf");
        fontNames = ArrayList(fontNameToTypefaceFile.keys)
    }
}