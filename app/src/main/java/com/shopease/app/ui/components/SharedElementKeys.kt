package com.shopease.app.ui.components

/**
 * Centralizes the shared-element key format so the List/Search screens and the
 * Detail screen can't drift out of sync (a mismatched key just silently fails
 * to animate, which is an easy bug to introduce with hand-written strings).
 */
object SharedElementKeys {
    fun productImage(productId: String) = "product-image-$productId"
}
