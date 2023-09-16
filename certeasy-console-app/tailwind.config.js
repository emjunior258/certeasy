/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#03258C",
        "primary-0.6": "rgba(3, 37, 140, 0.6)",
        "primary-0.4": "rgba(3, 37, 140, 0.4)",
        "primary-0.08": "rgba(3, 37, 140, 0.08)",
        red: "#FF0000",
        "red-0.6": "rgba(255, 0, 0, 0.6)",
        "red-light": "#FFEBEB",
        purple: "#D4C6FB",
        "black-0.4": "rgba(0, 0, 0, 0.4)",
        "black-0.3": "rgba(0, 0, 0, 0.3)",
        "black-0.6": "rgba(0, 0, 0, 0.6)",
        "gray-40": "#BCBCBC",
        "gray-30": "#DBDBDB",
        "gray-20": "#E6E6E6",
        "gray-10": "#F7F7F7",
      },
      fontFamily: {
        sans: ["Poppins"],
        display: ["Poppins"],
        body: ["Poppins"],
      },
      fontSize: {
        28: "28px",
      },
    },
  },
  plugins: [],
};
