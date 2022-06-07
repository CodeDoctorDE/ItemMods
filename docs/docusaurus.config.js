/** @type {import('@docusaurus/types').DocusaurusConfig} */
module.exports = {
    title: "ItemMods",
    tagline: "Add custom items/blocks to your game.",
    url: "https://itemmods.linwood.dev",
    baseUrl: "/",
    onBrokenLinks: "throw",
    onBrokenMarkdownLinks: "warn",
    favicon: "img/favicon.ico",
    organizationName: "CodeDoctorDE", // Usually your GitHub org/ name.
    projectName: "ItemMods", // Usually your repo name.
    trailingSlash: false,
    themeConfig: {
        colorMode: {
            defaultMode: 'dark',
            disableSwitch: false,
            respectPrefersColorScheme: true,
        },
        navbar: {
            title: "ItemMods",
            logo: {
                alt: "ItemMods Logo",
                src: "img/logo.svg",
            },
            items: [
                {
                    type: 'doc',
                    docId: 'user/intro',
                    label: 'Docs',
                    position: 'left'
                },
                {
                    type: 'doc',
                    docId: 'api/intro',
                    label: 'API',
                    position: 'left'
                },
                {
                    to: 'download',
                    label: 'Download',
                    position: 'left'
                },
                {
                    to: 'community',
                    position: 'left',
                    label: 'Community',
                },
                {to: "/blog", label: "Blog", position: "left"},
                {
                    type: 'dropdown',
                    label: 'More',
                    position: 'left',
                    items: [
                        {
                            label: 'JavaDocs',
                            href: '/apidocs',
                        },
                        {
                            label: "JavaDocs Snapshot",
                            href: "/apidocs-snapshot"
                        },
                        {
                            label: 'Discord',
                            href: 'https://go.linwood.dev/itemmods-discord',
                        },
                        {
                            label: 'GitHub',
                            href: 'https://github.com/CodeDoctorDE/ItemMods',
                        },
                        {
                            label: 'Blog',
                            href: 'https://linwood.dev/blog'
                        },
                        {
                            label: 'Crowdin',
                            href: 'https://translate.linwood.dev/ItemMods'
                        },
                        {
                            label: 'Twitter',
                            href: 'https://twitter.com/LinwoodCloud',
                        },
                        {
                            label: 'License',
                            href: 'https://github.com/CodeDoctorDE/ItemMods/blob/develop/LICENSE'
                        }
                    ],
                },
                {
                    type: 'docsVersionDropdown',
                    position: 'right',
                    dropdownItemsAfter: [{to: '/versions', label: 'All versions'}]
                },
                {
                    type: 'localeDropdown',
                    position: 'right',
                },
            ],
        },
        footer: {
            style: "dark",
            links: [
                {
                    title: "Docs",
                    items: [
                        {
                            label: "Tutorial",
                            to: "/docs",
                        },
                    ],
                },
                {
                    title: "Community",
                    items: [
                        {
                            label: "Discord",
                            href: "https://go.linwood.dev/itemmods-discord",
                        },
                        {
                            label: "Twitter",
                            href: "https://twitter.com/LinwoodCloud",
                        },
                    ],
                },
                {
                    title: "More",
                    items: [
                        {
                            label: "Blog",
                            to: "/blog",
                        },
                        {
                            label: "GitHub",
                            href: "https://github.com/CodeDoctorDE/ItemMods",
                        },
                    ],
                },
            ],
            copyright: `Copyright © ${new Date().getFullYear()} ItemMods.`,
        },
    },
    presets: [
        [
            "@docusaurus/preset-classic",
            {
                docs: {
                    sidebarPath: require.resolve("./sidebars.js"),
                    path: "docs",
                    routeBasePath: "docs",
                    id: "default",
                    // Please change this to your repo.
                    editUrl:
                        "https://github.com/CodeDoctorDE/ItemMods/edit/develop/docs/",
                },
                blog: {
                    showReadingTime: true,
                    // Please change this to your repo.
                    editUrl:
                        "https://github.com/CodeDoctorDE/ItemMods/edit/develop/docs/blog/",
                },
                theme: {
                    customCss: require.resolve("./src/css/custom.css"),
                },
            },
        ],
    ],
    plugins: [
        [
            '@docusaurus/plugin-content-docs',
            {
                id: 'community',
                path: 'community',
                routeBasePath: '/',
                sidebarPath: require.resolve('./sidebarsCommunity.js')

            },
        ],
    ],
    i18n: {
        defaultLocale: 'en',
        locales: ['en', 'de', 'fr', 'no', 'es', 'zh'],
    }
};
