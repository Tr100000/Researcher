import { defineConfig } from "vitepress";

export default defineConfig({
  title: "Researcher",
  description: "A (mostly) data-driven research system for Minecraft, inspired by Factorio.",
  cleanUrls: true,
  head: [["link", { rel: "icon", href: "/assets/icon.png" }]],
  themeConfig: {
    nav: [
      { text: "Home", link: "/" },
      { text: "Getting Started", link: "/dev/basics/getting-started" },
    ],

    sidebar: [
      { text: "What is Researcher?", link: "/what-is-researcher" },
      { text: "Roadmap", link: "/roadmap" },
      {
        text: "The Basics",
        items: [
          { text: "Getting Started", link: "/dev/basics/getting-started" },
          { text: "Adding a Research", link: "/dev/basics/adding-a-research" },
          { text: "Locking Recipes", link: "/dev/basics/locking-recipes" },
          { text: "Criteria", link: "/dev/basics/criteria" },
        ],
      },
      {
        text: "Advanced Guide",
        items: [
          { text: "Criterion Handlers", link: "/dev/advanced/criterion-handlers" },
          { text: "Locking Other Recipe Types", link: "/dev/advanced/locking-other-recipe-types" },
        ],
      },
    ],

    socialLinks: [{ icon: "github", link: "https://github.com/Tr100000/Researcher" }],
  },
});
