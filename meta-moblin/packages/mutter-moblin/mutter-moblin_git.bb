DESCRIPTION = "A Moblin specific plugin for the Mutter composite window manager"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "clutter-1.0 nbtk mutter gnome-menus mojito libjana anerley clutter-mozembed bickley"
PV = "2.25.2+git${SRCPV}"
PR = "r9"

SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://startup-notify.patch;patch=1 \
           file://88mutter-panelapps.sh \
           file://background-tile.png"

FILES_${PN} += "\
	${sysconfdir}/X11 \
	${libdir}/metacity/plugins/clutter/*.so* \
	${libdir}/mutter/plugins/*.so* \
	${datadir}/mutter-moblin-netbook-plugin \
	${datadir}/dbus-1/services \
	${datadir}/moblin-panel-applications/theme \
	${datadir}/moblin-panel-pasteboard/theme"
FILES_${PN}-dbg += "${libdir}/metacity/plugins/clutter/.debug/*"
FILES_${PN}-dbg += "${libdir}/mutter/plugins/.debug/*"

S = "${WORKDIR}/git"

ASNEEDED = ""

EXTRA_OECONF = "--enable-ahoghill --enable-netpanel --enable-people"

inherit autotools_stage

do_configure_prepend () {
	rm -f ${S}/build/autotools/gtk-doc.m4
	cp ${WORKDIR}/background-tile.png ${S}/data/theme/panel/
}

do_install_append () {
	install -d ${D}${sysconfdir}/X11/Xsession.d/
	install ${WORKDIR}/88mutter-panelapps.sh ${D}${sysconfdir}/X11/Xsession.d/
}


pkg_postinst_${PN} () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

. ${sysconfdir}/init.d/functions

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type list --list-type string --set /apps/metacity/general/clutter_plugins '[moblin-netbook]'

nbtk-create-image-cache ${datadir}/mutter-moblin/theme
}
