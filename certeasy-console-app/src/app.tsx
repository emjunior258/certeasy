import { Suspense } from 'react'
import { useRoutes } from 'react-router-dom'
import { Helmet, HelmetProvider } from 'react-helmet-async'
import { routes } from '@/routes'
import { ChakraProvider } from '@chakra-ui/react'
import theme from '@/theme'

export default function App() {
  return (
    <ChakraProvider value={theme}>
    <HelmetProvider>
      <Helmet
        title='The easiest certificate authority'
        titleTemplate='%s - Certeasy'
      />
      <Suspense fallback={<p>Loading...</p>}>
          {useRoutes(routes)}
      </Suspense>
    </HelmetProvider>
    </ChakraProvider>
  )
}
