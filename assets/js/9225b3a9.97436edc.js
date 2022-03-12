"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[446],{3905:function(e,t,a){a.d(t,{Zo:function(){return u},kt:function(){return g}});var n=a(7294);function l(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}function i(e,t){var a=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),a.push.apply(a,n)}return a}function r(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?arguments[t]:{};t%2?i(Object(a),!0).forEach((function(t){l(e,t,a[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(a)):i(Object(a)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(a,t))}))}return e}function o(e,t){if(null==e)return{};var a,n,l=function(e,t){if(null==e)return{};var a,n,l={},i=Object.keys(e);for(n=0;n<i.length;n++)a=i[n],t.indexOf(a)>=0||(l[a]=e[a]);return l}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(n=0;n<i.length;n++)a=i[n],t.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(e,a)&&(l[a]=e[a])}return l}var p=n.createContext({}),s=function(e){var t=n.useContext(p),a=t;return e&&(a="function"==typeof e?e(t):r(r({},t),e)),a},u=function(e){var t=s(e.components);return n.createElement(p.Provider,{value:t},e.children)},m={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},d=n.forwardRef((function(e,t){var a=e.components,l=e.mdxType,i=e.originalType,p=e.parentName,u=o(e,["components","mdxType","originalType","parentName"]),d=s(a),g=l,k=d["".concat(p,".").concat(g)]||d[g]||m[g]||i;return a?n.createElement(k,r(r({ref:t},u),{},{components:a})):n.createElement(k,r({ref:t},u))}));function g(e,t){var a=arguments,l=t&&t.mdxType;if("string"==typeof e||l){var i=a.length,r=new Array(i);r[0]=d;var o={};for(var p in t)hasOwnProperty.call(t,p)&&(o[p]=t[p]);o.originalType=e,o.mdxType="string"==typeof e?e:l,r[1]=o;for(var s=2;s<i;s++)r[s]=a[s];return n.createElement.apply(null,r)}return n.createElement.apply(null,a)}d.displayName="MDXCreateElement"},5142:function(e,t,a){a.r(t),a.d(t,{assets:function(){return u},contentTitle:function(){return p},default:function(){return g},frontMatter:function(){return o},metadata:function(){return s},toc:function(){return m}});var n=a(7462),l=a(3366),i=(a(7294),a(3905)),r=["components"],o={id:"config",title:"Config"},p=void 0,s={unversionedId:"config",id:"config",title:"Config",description:"Add set GitHubPagesPlugin in the build.sbt.",source:"@site/docs/config.md",sourceDirName:".",slug:"/config",permalink:"/docs/config",tags:[],version:"current",frontMatter:{id:"config",title:"Config"},sidebar:"someSidebar",previous:{title:"Getting Started",permalink:"/docs/"},next:{title:"Run to Publish",permalink:"/docs/run"}},u={},m=[{value:"Essential settings",id:"essential-settings",level:2},{value:"GitHub Org Name / Username *",id:"github-org-name--username-",level:3},{value:"GitHub Repo Name *",id:"github-repo-name-",level:3},{value:"Site Dir *",id:"site-dir-",level:3},{value:"More Settings",id:"more-settings",level:2},{value:"GitHub Pages Branch",id:"github-pages-branch",level:3},{value:"NoJekyll",id:"nojekyll",level:3},{value:"GitHubToken",id:"githubtoken",level:3},{value:"Dirs To Ignore",id:"dirs-to-ignore",level:3},{value:"Ignore Dot Dirs",id:"ignore-dot-dirs",level:3},{value:"Accepted Text Extensions",id:"accepted-text-extensions",level:3},{value:"Accepted Text MaxLength",id:"accepted-text-maxlength",level:3},{value:"Publish Commit Message",id:"publish-commit-message",level:3},{value:"Use GitHub Enterprise",id:"use-github-enterprise",level:2},{value:"GitHub Enterprise - Base URL",id:"github-enterprise---base-url",level:3},{value:"GitHub Enterprise - Authorize URL",id:"github-enterprise---authorize-url",level:3},{value:"GitHub Enterprise - Access Token URL",id:"github-enterprise---access-token-url",level:3},{value:"GitHub Enterprise - Headers",id:"github-enterprise---headers",level:3}],d={toc:m};function g(e){var t=e.components,a=(0,l.Z)(e,r);return(0,i.kt)("wrapper",(0,n.Z)({},d,a,{components:t,mdxType:"MDXLayout"}),(0,i.kt)("p",null,"Add set ",(0,i.kt)("inlineCode",{parentName:"p"},"GitHubPagesPlugin")," in the ",(0,i.kt)("inlineCode",{parentName:"p"},"build.sbt"),"."),(0,i.kt)("h2",{id:"essential-settings"},"Essential settings"),(0,i.kt)("p",null,"You probably need only these three setting keys in your project config to publish GitHub Pages."),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesSiteDir := baseDirectory.value / "path/to/github-pages-root"\n\n// These aren\'t mandatory unless sbt-github-pages can\'t infer them from `git remote`\ngitHubPagesOrgName := "USERNAME_OR_ORG",\ngitHubPagesRepoName := "YOUR_PROJECT",\n')),(0,i.kt)("p",null,"e.g.)"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'lazy val root = (project in file("."))\n  .enablePlugins(GitHubPagesPlugin)\n  .settings(\n    name := "YOUR_PROJECT",\n    gitHubPagesOrgName := "USERNAME_OR_ORG",\n    gitHubPagesRepoName := "YOUR_PROJECT",\n    gitHubPagesSiteDir := baseDirectory.value / "path/to/github-pages-root"\n  )\n')),(0,i.kt)("h3",{id:"github-org-name--username-"},"GitHub Org Name / Username *"),(0,i.kt)("div",{className:"admonition admonition-important alert alert--info"},(0,i.kt)("div",{parentName:"div",className:"admonition-heading"},(0,i.kt)("h5",{parentName:"div"},(0,i.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,i.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},(0,i.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"}))),"important")),(0,i.kt)("div",{parentName:"div",className:"admonition-content"},(0,i.kt)("p",{parentName:"div"},"This key must be set by the user of this plugin."))),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesOrgName")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"String")),(0,i.kt)("td",{parentName:"tr",align:null},"Value obtained from calling ",(0,i.kt)("inlineCode",{parentName:"td"},"git remote"))))),(0,i.kt)("p",null,"The GitHub organization name (or username) (i.e.",(0,i.kt)("inlineCode",{parentName:"p"},"OrgName")," from ",(0,i.kt)("inlineCode",{parentName:"p"},"https://github.com/OrgName/RepoName"),")."),(0,i.kt)("p",null,"Most of the times you won't need to set this setting, since ",(0,i.kt)("inlineCode",{parentName:"p"},"sbt-github-pages")," will try to read it from the ",(0,i.kt)("inlineCode",{parentName:"p"},"git remote")," information."),(0,i.kt)("p",null,"e.g.)"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesOrgName := "Kevin-Lee"\n')),(0,i.kt)("h3",{id:"github-repo-name-"},"GitHub Repo Name *"),(0,i.kt)("div",{className:"admonition admonition-important alert alert--info"},(0,i.kt)("div",{parentName:"div",className:"admonition-heading"},(0,i.kt)("h5",{parentName:"div"},(0,i.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,i.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},(0,i.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"}))),"important")),(0,i.kt)("div",{parentName:"div",className:"admonition-content"},(0,i.kt)("p",{parentName:"div"},"This key must be set by the user of this plugin."))),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesRepoName")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"String")),(0,i.kt)("td",{parentName:"tr",align:null},"Value obtained from calling ",(0,i.kt)("inlineCode",{parentName:"td"},"git remote"))))),(0,i.kt)("p",null,"The GitHub project repository name (i.e. ",(0,i.kt)("inlineCode",{parentName:"p"},"RepoName")," from ",(0,i.kt)("inlineCode",{parentName:"p"},"https://github.com/OrgName/RepoName"),")."),(0,i.kt)("p",null,"Most of the times you won't need to set this setting, since ",(0,i.kt)("inlineCode",{parentName:"p"},"sbt-github-pages")," will try to read it from the ",(0,i.kt)("inlineCode",{parentName:"p"},"git remote")," information."),(0,i.kt)("p",null,"e.g.)"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesRepoName := "sbt-github-pages"\n')),(0,i.kt)("h3",{id:"site-dir-"},"Site Dir *"),(0,i.kt)("div",{className:"admonition admonition-important alert alert--info"},(0,i.kt)("div",{parentName:"div",className:"admonition-heading"},(0,i.kt)("h5",{parentName:"div"},(0,i.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,i.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},(0,i.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"}))),"important")),(0,i.kt)("div",{parentName:"div",className:"admonition-content"},(0,i.kt)("p",{parentName:"div"},"This key must be set by the user of this plugin."))),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesSiteDir")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"File")),(0,i.kt)("td",{parentName:"tr",align:null})))),(0,i.kt)("p",null,"The folder contains all the files to be pushed to the GitHub Pages branch specified at ",(0,i.kt)("inlineCode",{parentName:"p"},"gitHubPagesBranch"),"."),(0,i.kt)("p",null,"e.g.)"),(0,i.kt)("p",null,"Project structure"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre"},"Project\nRoot \u2500\u252c\u2500 dirA\n      \u251c\u2500 dirB\n      \u2514\u2500 website\u2500\u252c\u2500 dirC\n                 \u251c\u2500 dirD\n                 \u251c\u2500 build <== Contains website files\n                 \u2514\u2500 node_modules\n")),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesSiteDir := (ThisBuild / baseDirectory).value / "website" / "build"\n')),(0,i.kt)("h2",{id:"more-settings"},"More Settings"),(0,i.kt)("h3",{id:"github-pages-branch"},"GitHub Pages Branch"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesBranch")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"String")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gh-pages"))))),(0,i.kt)("p",null,"A setting key to set the GitHub Pages branch. By default, it's ",(0,i.kt)("inlineCode",{parentName:"p"},"gh-pages")," but if your project repos uses a different branch you can set it up using this key."),(0,i.kt)("p",null,"e.g.) If you want to use ",(0,i.kt)("inlineCode",{parentName:"p"},"main")," branch instead of ",(0,i.kt)("inlineCode",{parentName:"p"},"gh-pages"),","),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesBranch := "main"\n')),(0,i.kt)("h3",{id:"nojekyll"},"NoJekyll"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesNoJekyll")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"Boolean")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"true"))))),(0,i.kt)("p",null,"The flag to decide whether to add ",(0,i.kt)("inlineCode",{parentName:"p"},".nojekyll"),"."),(0,i.kt)("p",null,"GitHub Pages, by default, uses Jekyll to generate a static website. However, ",(0,i.kt)("inlineCode",{parentName:"p"},"sbt-github-pages")," is designed to publish a ready to go website to GitHub Pages. So it's necessary to tell GitHub Pages not to use Jeklly and ask to use whatever is sent to the GitHub Pages branch as is. It can be done by placing a ",(0,i.kt)("inlineCode",{parentName:"p"},".nojekyll")," file."),(0,i.kt)("p",null,"If you want GitHub Pages to use Jekyll and generate a static website, set ",(0,i.kt)("inlineCode",{parentName:"p"},"gitHubPagesNoJekyll")," to ",(0,i.kt)("inlineCode",{parentName:"p"},"false"),"."),(0,i.kt)("p",null,"e.g.) Let GitHub Pages use Jekyll to generate a website."),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},"gitHubPagesNoJekyll := false\n")),(0,i.kt)("h3",{id:"githubtoken"},"GitHubToken"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesGitHubToken")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"Option[String]")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},'sys.env.get("GITHUB_TOKEN")'))))),(0,i.kt)("div",{className:"admonition admonition-danger alert alert--danger"},(0,i.kt)("div",{parentName:"div",className:"admonition-heading"},(0,i.kt)("h5",{parentName:"div"},(0,i.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,i.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"12",height:"16",viewBox:"0 0 12 16"},(0,i.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M5.05.31c.81 2.17.41 3.38-.52 4.31C3.55 5.67 1.98 6.45.9 7.98c-1.45 2.05-1.7 6.53 3.53 7.7-2.2-1.16-2.67-4.52-.3-6.61-.61 2.03.53 3.33 1.94 2.86 1.39-.47 2.3.53 2.27 1.67-.02.78-.31 1.44-1.13 1.81 3.42-.59 4.78-3.42 4.78-5.56 0-2.84-2.53-3.22-1.25-5.61-1.52.13-2.03 1.13-1.89 2.75.09 1.08-1.02 1.8-1.86 1.33-.67-.41-.66-1.19-.06-1.78C8.18 5.31 8.68 2.45 5.05.32L5.03.3l.02.01z"}))),"Warning!!!")),(0,i.kt)("div",{parentName:"div",className:"admonition-content"},(0,i.kt)("p",{parentName:"div"},"DO NOT hard-code the actual GitHub auth token String here.\nUse an environment variable instead."))),(0,i.kt)("p",null,"The GitHub authentication token (default: The value from environment variable ",(0,i.kt)("inlineCode",{parentName:"p"},"GITHUB_TOKEN"),")\nIf you're using GitHub Actions to publish, you don't need to set it up at all.\nGitHub provides the GitHub token in GitHub Actions build so you can just use it."),(0,i.kt)("p",null,"e.g.)\nIn your GitHub Actions config yml file,"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-yml"},'jobs:\n  build:\n\n    runs-on: ubuntu-latest\n\n    steps:\n      - uses: actions/checkout@v2\n\n# ...\n\n      - name: Publish website\n        env:\n          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}\n        run: |\n          echo "> Publish GitHub Pages"\n          sbt publishToGitHubPages\n')),(0,i.kt)("p",null,"e.g.) If you want to change it,"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesGitHubToken := sys.env.get("SOME_OTHER_VAR")\n')),(0,i.kt)("div",{className:"admonition admonition-danger alert alert--danger"},(0,i.kt)("div",{parentName:"div",className:"admonition-heading"},(0,i.kt)("h5",{parentName:"div"},(0,i.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,i.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"12",height:"16",viewBox:"0 0 12 16"},(0,i.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M5.05.31c.81 2.17.41 3.38-.52 4.31C3.55 5.67 1.98 6.45.9 7.98c-1.45 2.05-1.7 6.53 3.53 7.7-2.2-1.16-2.67-4.52-.3-6.61-.61 2.03.53 3.33 1.94 2.86 1.39-.47 2.3.53 2.27 1.67-.02.78-.31 1.44-1.13 1.81 3.42-.59 4.78-3.42 4.78-5.56 0-2.84-2.53-3.22-1.25-5.61-1.52.13-2.03 1.13-1.89 2.75.09 1.08-1.02 1.8-1.86 1.33-.67-.41-.66-1.19-.06-1.78C8.18 5.31 8.68 2.45 5.05.32L5.03.3l.02.01z"}))),"Warning!!!")),(0,i.kt)("div",{parentName:"div",className:"admonition-content"},(0,i.kt)("p",{parentName:"div"},"DO NOT hard-code the actual GitHub auth token String here.\nUse an environment variable instead."))),(0,i.kt)("h3",{id:"dirs-to-ignore"},"Dirs To Ignore"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesDirsToIgnore")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"Set[String]")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},'Set("target", "bin", "output")'))))),(0,i.kt)("p",null,"A list of directory names to be ignored when committing to the GitHub Pages branch."),(0,i.kt)("p",null,"e.g.) You add more or remove some."),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesDirsToIgnore := Set("node_modules", "build", "output")\n')),(0,i.kt)("h3",{id:"ignore-dot-dirs"},"Ignore Dot Dirs"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesIgnoreDotDirs")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"Boolean")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"true"))))),(0,i.kt)("p",null,"A flag to indicate whether to ignore or not dot directories when committing to the GitHub Pages branch."),(0,i.kt)("p",null,"e.g.) To include all the dot dirs like ",(0,i.kt)("inlineCode",{parentName:"p"},".cache"),", ",(0,i.kt)("inlineCode",{parentName:"p"},".github"),", ",(0,i.kt)("inlineCode",{parentName:"p"},".idea"),", etc., set it to ",(0,i.kt)("inlineCode",{parentName:"p"},"false"),"."),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},"gitHubPagesIgnoreDotDirs := false\n")),(0,i.kt)("h3",{id:"accepted-text-extensions"},"Accepted Text Extensions"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesAcceptedTextExtensions")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"Set[String]")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},'Set(".md", ".css", ".html", ".xml", ".js", ".txt")'))))),(0,i.kt)("p",null,"Accepted text files to create ",(0,i.kt)("inlineCode",{parentName:"p"},"UTF-8")," encoded ",(0,i.kt)("inlineCode",{parentName:"p"},"String")," based blob.\nIf the file's extension is not one of these, ",(0,i.kt)("inlineCode",{parentName:"p"},"Base64")," encoded blob is created."),(0,i.kt)("p",null,"Default:"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'Set(".md", ".css", ".html", ".xml", ".js", ".txt")\n')),(0,i.kt)("p",null,"e.g.) You add more or remove some."),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesAcceptedTextExtensions :=\n  Set(".json", ".js", ".css", ".html")\n')),(0,i.kt)("h3",{id:"accepted-text-maxlength"},"Accepted Text MaxLength"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesAcceptedTextMaxLength")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"Int")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"10240"))))),(0,i.kt)("p",null,"The max length of the bytes (",(0,i.kt)("inlineCode",{parentName:"p"},"Array[Byte]"),") of the file to commit.\nIf the file byte size is greater than this, ",(0,i.kt)("inlineCode",{parentName:"p"},"Base64")," encoded blob is created."),(0,i.kt)("p",null,"e.g.) You can make it smaller."),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},"gitHubPagesAcceptedTextMaxLength := 6144\n")),(0,i.kt)("h3",{id:"publish-commit-message"},"Publish Commit Message"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesPublishCommitMessage")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"String")),(0,i.kt)("td",{parentName:"tr",align:null},"ENV VAR ",(0,i.kt)("inlineCode",{parentName:"td"},"GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE")," or ",(0,i.kt)("inlineCode",{parentName:"td"},"Updated ${gitHubPagesBranch.value}"))))),(0,i.kt)("p",null,"The commit message when publish to GitHub Pages. "),(0,i.kt)("p",null,"First, it tries to get the value from the environment variable ",(0,i.kt)("inlineCode",{parentName:"p"},"GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE"),".\nIf not found, the default value is ",(0,i.kt)("inlineCode",{parentName:"p"},'s"Updated ${gitHubPagesBranch.value}"')),(0,i.kt)("p",null,"Default:"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesPublishCommitMessage :=\n  sys.env.getOrElse(\n    "GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE",\n    s"Updated ${gitHubPagesBranch.value}"\n  )\n')),(0,i.kt)("p",null,"e.g.) If you want to have a different message, you can change it."),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesPublishCommitMessage := s"New stuff in my awesome website!!!"\n')),(0,i.kt)("h2",{id:"use-github-enterprise"},"Use GitHub Enterprise"),(0,i.kt)("p",null,(0,i.kt)("inlineCode",{parentName:"p"},"sbt-github-pages")," support GitHub Enterprise"),(0,i.kt)("p",null,"There are four properties can be used to use GitHub Enterprise.\nTo set these up, get the right values from your company."),(0,i.kt)("ul",null,(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"gitHubPagesGitHubBaseUrl")),(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"gitHubPagesGitHubAuthorizeUrl")),(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"gitHubPagesGitHubAccessTokenUrl")),(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"gitHubPagesGitHubHeaders"))),(0,i.kt)("h3",{id:"github-enterprise---base-url"},"GitHub Enterprise - Base URL"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesGitHubBaseUrl")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"String")),(0,i.kt)("td",{parentName:"tr",align:null},"ENV VAR ",(0,i.kt)("inlineCode",{parentName:"td"},"GITHUB_ENT_BASE_URL")," or ",(0,i.kt)("inlineCode",{parentName:"td"},"https://api.github.com/"))))),(0,i.kt)("p",null,"NOTE: The trailing slash is significant. So ",(0,i.kt)("inlineCode",{parentName:"p"},"https://some.url.blah")," does not work. It should be ",(0,i.kt)("inlineCode",{parentName:"p"},"https://some.url.blah/"),"."),(0,i.kt)("p",null,"e.g.)"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-shell"},'export GITHUB_ENT_BASE_URL="https://github.my-company.internal/api/v3/"\n')),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesGitHubBaseUrl := "https://github.my-company.internal/api/v3/"\n')),(0,i.kt)("h3",{id:"github-enterprise---authorize-url"},"GitHub Enterprise - Authorize URL"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesGitHubAuthorizeUrl")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"String")),(0,i.kt)("td",{parentName:"tr",align:null},"ENV VAR ",(0,i.kt)("inlineCode",{parentName:"td"},"GITHUB_ENT_AUTHORIZE_URL")," or ",(0,i.kt)("inlineCode",{parentName:"td"},"https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"))))),(0,i.kt)("p",null,"e.g.)"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-shell"},'export GITHUB_ENT_AUTHORIZE_URL="https://github.my-company.internal/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"\n')),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesGitHubAuthorizeUrl :=\n  "https://github.my-company.internal/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"\n')),(0,i.kt)("h3",{id:"github-enterprise---access-token-url"},"GitHub Enterprise - Access Token URL"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesGitHubAccessTokenUrl")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"String")),(0,i.kt)("td",{parentName:"tr",align:null},"ENV VAR ",(0,i.kt)("inlineCode",{parentName:"td"},"GITHUB_ENT_ACCESS_TOKEN_URL")," or ",(0,i.kt)("inlineCode",{parentName:"td"},"https://github.com/login/oauth/access_token"))))),(0,i.kt)("p",null,"e.g.)"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-shell"},'export GITHUB_ENT_ACCESS_TOKEN_URL="https://github.my-company.internal/login/oauth/access_token"\n')),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesGitHubAccessTokenUrl :=\n  "https://github.my-company.internal/login/oauth/access_token"\n')),(0,i.kt)("h3",{id:"github-enterprise---headers"},"GitHub Enterprise - Headers"),(0,i.kt)("table",null,(0,i.kt)("thead",{parentName:"table"},(0,i.kt)("tr",{parentName:"thead"},(0,i.kt)("th",{parentName:"tr",align:null},"Name"),(0,i.kt)("th",{parentName:"tr",align:null},"Value Type"),(0,i.kt)("th",{parentName:"tr",align:null},"Default"))),(0,i.kt)("tbody",{parentName:"table"},(0,i.kt)("tr",{parentName:"tbody"},(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"gitHubPagesGitHubHeaders")),(0,i.kt)("td",{parentName:"tr",align:null},(0,i.kt)("inlineCode",{parentName:"td"},"Map[String, String]")),(0,i.kt)("td",{parentName:"tr",align:null},"ENV VAR ",(0,i.kt)("inlineCode",{parentName:"td"},"GITHUB_ENT_HEADERS")," or ",(0,i.kt)("inlineCode",{parentName:"td"},'Map("User-Agent" -> "github4s")'))))),(0,i.kt)("p",null,"e.g.)\nWhen using the environment variable, the value should be JSON containing String key to String value pairs.\ne.g.)"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-shell"},'export GITHUB_ENT_HEADERS=\'{"User-Agent":"app-doc-publisher", "something-else":"blah"}\'\n')),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-scala"},'gitHubPagesGitHubHeaders := Map("User-Agent" -> "app-doc-publisher")\n')))}g.isMDXComponent=!0}}]);