/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: '#FF6600',
        secondary: '#13357B',
      },
      screens: {
        xs: '480px',
  			sm: '640px',
  			md: '768px',
  			lg: '1024px',
  			xl: '1280px',
  			'2xl': '1536px'
      }
    },
  },
  plugins: [
     function({ addUtilities }) {
      addUtilities({
        '.scrollbar-hide': {
          'scrollbar-width': 'none', /* Firefox */
          '-ms-overflow-style': 'none', /* IE 10+ */
          '&::-webkit-scrollbar': {
            display: 'none', /* Chrome, Safari */
          },
        },
        '.scrollbar-thin-gray': {
          'scrollbar-width': 'thin',
          '&::-webkit-scrollbar': { width: '6px' },
          '&::-webkit-scrollbar-thumb': {
            backgroundColor: '#9ca3af', // gray-400
            borderRadius: '8px',
          },
        },
        '.scrollbar-hover': {
        'scrollbar-width': 'thin',
        'scrollbar-color': 'transparent transparent',
        '&::-webkit-scrollbar': {
          width: '6px',
          background: 'transparent',
        },
        '&::-webkit-scrollbar-thumb': {
          background: 'transparent',
          borderRadius: '8px',
          transition: 'background 0.3s',
        },
        '&:hover::-webkit-scrollbar-thumb': {
          background: '#9ca3af',
        },
        '&::-webkit-scrollbar-button': {
          display: 'none', 
        },
        '&:hover': {
          'scrollbar-color': '#9ca3af transparent',
        },
        },
      })
    },
  ],
}

