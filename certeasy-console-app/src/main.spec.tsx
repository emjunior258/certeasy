import { describe, expect, it } from 'vitest'
import { waitFor } from '@testing-library/react'

describe("main.tsx", () => {
    it("should mount the application on #root element", async () => {
        const root = document.createElement('div')
        root.id = "root"
        const body = document.querySelector('body')
        body?.appendChild(root)
        await import("./main")
        await waitFor(() => expect(document.title).toBe('The easiest certificate authority - Certeasy'))
    })
})