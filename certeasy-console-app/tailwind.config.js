/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [],
  purge: ["./index.html", "./src/**/*{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#03258C",
        purple: "#D4C6FB",
      },
    },
  },
  plugins: [],
};
