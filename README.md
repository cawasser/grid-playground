# grid-playground

(based on shadow-cljs - browser quickstart)



## Purpose

This "version" (branch) holds the development of a reactive-grid-layout written on pure ClojureScript. The goal is to eliminate
any dependencies on Javascript and React, giving us much more control over exactly how our layout handles updates to the number
of widgets (add/remove) their visual layout (where on the display), and the content (data form subscriptions).

In the past we've had trouble with components not handling all three of these cases really well. For example, and update to a subscribed  
source, which should only impact a single widget, forces a complete re-draw of everything, the grid AND all the widgets. This is, for  
many reasons, unacceptable.



## Todo List

- [x] drag "widgets"
- [ ] drop widgets
- [ ] resize widgets
- [ ] more extensive API (callbacks for saving layout, etc.)
- [ ] grid "compaction"

## Wish List?

????
