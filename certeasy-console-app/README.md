# Certeasy Console Application

The easiest certificate authority for development and experiments.

## Requirements

Bellow are the requirements in order to contribute or build this project:

- `NodeJs >= 18`
- `pnpm >= 9`

> **ℹ️ Note**  
> We recommend to use `corepack` to install `pnpm` in your development machine by using the `corepack enable` command.

## Project structure

| Path             | Description                                                                                                                                 |
| ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| `src`            | Source code                                                                                                                                 |
| `src/components` |                                                                                                                                             |
| `src/pages`      | File-system based router built on concepts of pages. When a file is added to the `pages` directory it's automatically available as a route. |
| `src/providers`  |                                                                                                                                             |
| `src/assets`     |                                                                                                                                             |

## Design Reference

For design references, you can view the application's Figma design [here](https://www.figma.com/design/rGsbR1OMGuDyvWuPFD8XOb/Certeasy-%7C-DRAFT?node-id=0-1&node-type=canvas&t=k6Vr4fb9hP1ojMda-0).
This includes the visual layouts and component guidelines that align with the Certeasy Console Application's user interface.

## Setting up your development environment

1. Open a new terminal window
2. Clone the repository
3. Go to the `certeasy-console-app project`
   ```sh
   cd certeasy/certeasy-console-app
   ```
4. Install dependencies
   ```sh
   pnpm install
   ```

### Starting the development server

Assuming that you have already [set up you development environment](#setting-up-your-development-environment), run `pnpm dev` to start the development server.

### Building the application

Assuming that you have already [set up you development environment](#setting-up-your-development-environment), run `pnpm build` to build the final bundle of application.
