const algoliaConfig = require('./algolia.config.json');

const isEmptyObject = obj => {
  for (field in obj) return false;
  return true;
};

const isSearchable = !isEmptyObject(algoliaConfig)

const websiteConfig = {
  title: 'sbt-github-pages',
  tagline: 'Publish GitHub Pages with minimum effort',
  url: 'https://kevin-lee.github.io/sbt-github-pages',
  baseUrl: '/',
  favicon: 'img/favicon.ico',
  organizationName: 'Kevin-Lee', // Usually your GitHub org/user name.
  projectName: 'sbt-github-pages', // Usually your repo name.
  themeConfig: {
    prism: {
      additionalLanguages: ['scala'],
    },
    navbar: {
      title: 'sbt-github-pages',
      logo: {
        alt: 'sbt-github-pages Logo',
        src: 'img/logo.svg',
      },
      links: [
        {
          to: 'docs/',
          activeBasePath: 'docs',
          label: 'Docs',
          position: 'left',
        },
        {
          href: 'https://github.com/Kevin-Lee/sbt-github-pages',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'Getting Started',
              to: 'docs/',
            },
            {
              label: 'Config',
              to: 'docs/config/',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/Kevin-Lee/sbt-github-pages',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} sbt-github-pages, Website Built with Docusaurus.`,
    },
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          // It is recommended to set document id as docs home page (`docs/` path).
          homePageId: 'getting-started',
          sidebarPath: require.resolve('./sidebars.js'),
          // Please change this to your repo.
          // editUrl:
          //   'https://github.com/Kevin-Lee/sbt-github-pages/edit/main/website/',
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};

if (isSearchable) {
  websiteConfig['themeConfig']['algolia'] = algoliaConfig;
}

module.exports = websiteConfig;