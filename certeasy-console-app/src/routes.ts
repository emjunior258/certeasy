import { Layout } from '@/components/ui/layout'
import React from 'react'
import { RouteObject } from 'react-router'
import fsRoutes from '~react-pages'

export const routes: RouteObject[] = [
    {
        path: '/',
        element: React.createElement(Layout),
        children: fsRoutes
    }
]