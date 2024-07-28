import { Fragment, Suspense } from 'react'
import { useRoutes } from 'react-router-dom'
import { createGlobalStyle } from 'styled-components'
import { theme } from './theme'
import 'reset-css'

const GlobalStyle = createGlobalStyle`
  body {
    font-family: ${theme.fontFamily.body};
    font-weight: ${theme.fontWeight.normal};
  }
`

import routes from '~react-pages'

export default function App() {
  return (
    <Fragment>
      <GlobalStyle />
      <Suspense fallback={<p>Loading...</p>}>
        {useRoutes(routes)}
      </Suspense>
    </Fragment>
  )
}