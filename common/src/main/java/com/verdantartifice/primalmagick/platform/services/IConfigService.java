package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftSpeed;

public interface IConfigService {
    /**
     * Determine whether item affinities should be shown by default.
     *
     * @return true if item affinities should be shown by default, false if they should instead require the appropriate
     * key to be pressed
     */
    boolean showAffinities();

    /**
     * Determine whether the wand HUD should be shown when the player has a wand in hand.
     *
     * @return true if the wand HUD should be shown, false otherwise
     */
    boolean showWandHud();

    /**
     * Determine whether radial menus will select the highlighted item upon release of the menu key.
     *
     * @return true if the menu will select upon key release, false if it instead requires a click
     */
    boolean radialReleaseToSwitch();

    /**
     * Determine whether radial menus will try to prevent the mouse cursor from leaving the outer circle.
     *
     * @return true if the menu will try to clip the mouse position, false otherwise
     */
    boolean radialClipMouse();

    /**
     * Determine whether radial menus will allow clicking outside the outer circle to select the highlighted item.
     *
     * @return true if outer clicks are recognized, false otherwise
     */
    boolean radialAllowClickOutsideBounds();

    /**
     * Determine whether blocks and items should show their affinities in tooltips even without having been scanned.
     *
     * @return true if block/item affinities should be shown before scanning, false otherwise
     */
    boolean showUnscannedAffinities();

    /**
     * Get the progress rate modifier for Research Table theory yields.
     *
     * @return the progress rate modifier for Research Table theory yields
     */
    TheorycraftSpeed theorycraftSpeed();
}
