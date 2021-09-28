/** @type {import('@docusaurus/types').DocusaurusConfig} */
module.exports = {
  title: "ItemMods",
  tagline: "Add custom items/blocks to your game.",
  url: "https://itemmods.linwood.dev",
  baseUrl: "/",
  onBrokenLinks: "throw",
  onBrokenMarkdownLinks: "warn",
  favicon: "img/favicon.ico",
  organizationName: "CodeDoctorDE", // Usually your GitHub org/user name.
  projectName: "ItemMods", // Usually your repo name.
  themeConfig: {
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
        { to: "/blog", label: "Blog", position: "left" },
        /*{
          label: "Addons",
          to: "addons",
        },
        {
          label: "Mods",
          to: "mods",
        },*/
        {
          type: 'docsVersionDropdown',
          position: 'right',
          dropdownItemsAfter: [{to: '/versions', label: 'All versions'}]
        },
        {
          type: 'localeDropdown',
          position: 'right',
        },
        {
          href: "https://github.com/CodeDoctorDE/ItemMods",
          label: "GitHub",
          position: "right",
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
  i18n: {
    defaultLocale: 'en',
    locales: ['en', 'de', 'fr', 'no', 'es'],
  }
};
