/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [],
  purge: ["./index.html", "./src/**/*{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#03258C",
        "primary-0.6": "rgba(3, 37, 140, 0.6)",
        "blue-light": "#EBEEF6",
        red: "#FF0000",
        "red-0.6": "rgba(255, 0, 0, 0.6)",
        "red-light": "#FFEBEB",
        purple: "#D4C6FB",
      },
      fontFamily: {
        sans: ["Poppins"],
        display: ["Poppins"],
        body: ["Poppins"],
      },
    },
  },
  plugins: [],
};
