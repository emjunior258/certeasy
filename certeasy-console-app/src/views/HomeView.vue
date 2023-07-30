<template>
  <header class="bg-primary px-16 py-4 text-white">
    <div class="flex justify-between">
      <img
        src="~../assets/logo.png"
        alt="Certeasy"
        class="h-8"
      />
      <nav class="flex items-center">
        <ul class="flex items-center gap-4">
          <li>Open API</li>
          <li>Settings</li>
        </ul>
      </nav>
    </div>
  </header>
  <section>
    <div class="flex justify-between px-16 mt-12 mb-3">
      <div>
        <button
          class="rounded py-1 px-4 bg-primary text-white"
          type="button"
        >
          All (16)</button
        ><button
          class="rounded py-1 px-4"
          type="button"
        >
          Root (9)</button
        ><button
          class="rounded py-1 px-4"
          type="button"
        >
          Sub (7)</button
        ><button
          class="rounded py-1 px-4"
          type="button"
        >
          Tree
        </button>
      </div>
      <div class="flex gap-16">
        <button
          class="rounded py-3 px-6 bg-primary text-white"
          type="button"
        >
          New Root CA
        </button>
        <button
          class="rounded py-3 px-6"
          type="button"
        >
          Import CA
        </button>
      </div>
    </div>
    <ul class="px-16">
      <li class="border border-gray-200 rounded shadow px-11 py-6">
        <div class="flex justify-between">
          <div class="flex gap-6">
            <img
              src="https://placehold.co/52x52"
              alt="Issuer photo"
              class="h-13 w-13 border border-primary rounded"
            />
            <div class="flex flex-col justify-between items-start">
              <h3>My Issuer Name</h3>
              <span class="rounded-full py-0.5 px-2.5 bg-purple">Root</span>
            </div>
          </div>
          <div class="flex items-center gap-4">
            <button>download</button>
            <button>delete</button>
          </div>
        </div>
      </li>
    </ul>
  </section>
  <footer class="px-16 fixed bottom-0 w-full">
    <div class="border-t text-right pt-8 pb-11">
      <a href="#"> Read Documentation</a> <a href="#"> Report Issue</a>
    </div>
  </footer>
</template>

<script>
// @ is an alias to /src

export default {
  name: "HomeView",
};
</script>
