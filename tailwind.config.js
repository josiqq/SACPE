/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/flowbite/dist/flowbite.min.js"
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('flowbite/plugin')
  ],
}