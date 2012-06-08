function initExpandables() {
    jQuery(".expandable").expander({
        slicePoint: 200,
        expandText: "leer más",
        userCollapseText: "leer menos",
        expandEffect: 'show',
        collapseEffect: 'hide'
    });
}