import { Issuer } from '@/hooks/issuers'
import { http, HttpResponse } from 'msw'

const issuers = [
    {
      id: "7a0ebcf787242db56662ccd1824bf1c20adc3e31",
      name: "Root CA Example",
      type: "ROOT",
      serial: "1731585376629",
      dn: "CN=example.co.mz, C=MZ, ST=Maputo, L=Magoanine A, STREET=Av. Lurdes Mutola",
      path_length: -1,
      children_count: 1
    },
    {
      id: "542ebb4cbf35ba6f44d6dbd7da7993b89426bf2e",
      name: "Sub CA Example",
      type: "SUB_CA",
      serial: "1731593572672",
      dn: "CN=contoso.example.co.mz, O=Apple Inc, OU=Software Engineering Department, C=MZ, ST=Maputo, L=Boane, STREET=1234 Main Street",
      path_length: 0,
      parent: {
        id: "7a0ebcf787242db56662ccd1824bf1c20adc3e31",
        name: "Root CA Example"
      },
      children_count: 0
    }
  ]satisfies Issuer[]

export const handlers = [
    http.get('/api/issuers', () => {
        return HttpResponse.json(issuers)
    }),
]