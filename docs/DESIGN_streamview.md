---
name: StreamView System
colors:
  surface: '#131313'
  surface-dim: '#131313'
  surface-bright: '#393939'
  surface-container-lowest: '#0e0e0e'
  surface-container-low: '#1b1c1c'
  surface-container: '#202020'
  surface-container-high: '#2a2a2a'
  surface-container-highest: '#353535'
  on-surface: '#e5e2e1'
  on-surface-variant: '#c5c5d4'
  inverse-surface: '#e5e2e1'
  inverse-on-surface: '#303030'
  outline: '#8f909e'
  outline-variant: '#454652'
  surface-tint: '#bac3ff'
  primary: '#bac3ff'
  on-primary: '#08218a'
  primary-container: '#3f51b5'
  on-primary-container: '#cacfff'
  inverse-primary: '#4355b9'
  secondary: '#b4cad6'
  on-secondary: '#1e333c'
  secondary-container: '#374c56'
  on-secondary-container: '#a6bcc7'
  tertiary: '#ffba38'
  on-tertiary: '#432c00'
  tertiary-container: '#795300'
  on-tertiary-container: '#ffc971'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#dee0ff'
  primary-fixed-dim: '#bac3ff'
  on-primary-fixed: '#00105c'
  on-primary-fixed-variant: '#293ca0'
  secondary-fixed: '#cfe6f2'
  secondary-fixed-dim: '#b4cad6'
  on-secondary-fixed: '#071e27'
  on-secondary-fixed-variant: '#354a53'
  tertiary-fixed: '#ffdeac'
  tertiary-fixed-dim: '#ffba38'
  on-tertiary-fixed: '#281900'
  on-tertiary-fixed-variant: '#604100'
  background: '#131313'
  on-background: '#e5e2e1'
  surface-variant: '#353535'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 57px
    fontWeight: '400'
    lineHeight: 64px
    letterSpacing: -0.25px
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
  headline-sm:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '500'
    lineHeight: 32px
  title-lg:
    fontFamily: Inter
    fontSize: 22px
    fontWeight: '500'
    lineHeight: 28px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: 0.15px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.5px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.25px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.1px
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.5px
  title-md-mobile:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 16px
  margin-mobile: 16px
  margin-desktop: 32px
---

## Brand & Style

The design system is engineered for professional-grade video monitoring and stream management. It adopts a **Modern Corporate** aesthetic that prioritizes utility and focus, reminiscent of high-end developer tools or broadcast engineering suites. 

The visual narrative is built on the following principles:
- **Precision:** Rigorous alignment and consistent stroke weights.
- **Neutrality:** A sophisticated palette that allows video content to remain the focal point.
- **Clarity:** Information-dense layouts that remain legible through systematic typography and generous white space.
- **Technical Sophistication:** Subtle use of Material 3 surface elevations and refined interactions to provide a premium, tool-like feel.

## Colors

The color system centers on **Deep Indigo** as the primary functional color, signifying action and importance without the clinical feel of pure blue. The neutrals use **Warm Charcoal** for deep surfaces and **Muted Slate** for secondary text and borders, creating a low-fatigue environment for long-duration monitoring.

**Accent Usage:**
- **Primary (#3F51B5):** Key actions, active states, and focus indicators.
- **Secondary (#455A64):** Subtle UI elements, inactive tabs, and secondary buttons.
- **Tertiary (#FFB300):** Used sparingly for critical warnings or highlight metadata (Soft Amber).
- **Surface:** Uses a tonal palette based on the neutral charcoal, shifting slightly lighter as elevation increases per Material 3 specifications.

## Typography

The system utilizes **Inter** across all levels for its exceptional legibility in technical interfaces and neutral character. 

- **Stream Names:** Always use `title-md` to ensure they stand out in the grid.
- **Metadata:** Use `body-md` for technical specs and timestamps to maintain a clean hierarchy.
- **Condensed Data:** Use `label-sm` for status indicators and small pills where space is at a premium.
- **Responsiveness:** On mobile devices, stream titles should scale down to `title-md-mobile` to prevent excessive truncation in grid views.

## Layout & Spacing

This design system follows a **4px baseline grid** with a fluid 12-column system for desktop and a 4-column system for mobile.

- **Content Reflow:** Stream cards should maintain a 16:9 aspect ratio and reflow from a 4-column grid (Desktop) to a 2-column or 1-column grid (Mobile) depending on available width.
- **Margins:** Standardize on 16px margins for mobile devices and 32px for desktop to maintain the professional, spacious feel.
- **Touch Targets:** All interactive elements must maintain a minimum 48x48dp hit area, even if the visual representation is smaller.

## Elevation & Depth

In accordance with Material 3, depth is primarily communicated through **Tonal Layers** rather than heavy shadows.

- **Level 0 (Surface):** The base layer (#212121 in dark mode). Used for the main background.
- **Level 1 (Raised):** Used for StreamCards and secondary containers. Achieved by applying a 5% primary color overlay on the surface color.
- **Level 2 (Search/Dialogs):** Used for the TopBar and Floating Action Buttons. Achieved with a 8% primary color overlay.
- **Shadows:** When necessary for distinct separation (e.g., dropdowns), use highly diffused, low-opacity shadows (Blur: 12px, Opacity: 15% Black).

## Shapes

The design system uses a "Rounded" strategy (8px - 16px) to soften the technical nature of the app and provide a modern, high-end feel.

- **Small Components:** 4px (Checkboxes, small tooltips).
- **Medium Components:** 8px (Buttons, Input Fields).
- **Large Components:** 16px (StreamCards, Modal Dialogs, Video Containers).
- **Full:** Used for StatusPills and Search Bars to create the "pill" aesthetic.

## Components

### StreamCard
- **Thumbnail:** 16:9 ratio, 16px corner radius.
- **Overlay:** StatusPill in the top-right corner, 8px padding from edges.
- **Typography:** `title-md` for the stream name directly below the thumbnail, followed by `body-md` for metadata (e.g., "1080p • 60fps").

### StatusPill
- **Layout:** Small pill shape, 24px height. 
- **Indicator:** 8px circular dot. Hex: `#4CAF50` (Connected), `#B0BEC5` (Disconnected).
- **Label:** `label-sm` uppercase text.

### TopBar
- **Background:** Level 2 Surface elevation.
- **Search Field:** Expandable component. When collapsed, only the icon is visible. When expanded, it takes a pill shape with a subtle Level 1 background.
- **Stroke:** 1px bottom border using Muted Slate at 20% opacity.

### Loading & Error States
- **LoadingView:** Use shimmer patterns on 16:9 blocks to mimic StreamCards. Shimmer should move from left to right in a 2-second loop.
- **ErrorView:** Centered layout. Icon (2px stroke, 48dp) followed by `headline-sm`. Action button uses the Primary Indigo fill.
- **EmptyView:** Minimalist vector illustration in Muted Slate, centered, with `body-lg` descriptive text.

### Player Controls
- **Style:** Semi-transparent black overlay (60% opacity) on the bottom third of the video.
- **Icons:** 24dp size, 2px stroke weight.
- **Progress Bar:** 4px height. Primary Indigo for elapsed time, Muted Slate for remaining time. The "scrubber" knob only appears on hover/touch.