<template lang="pug">
  #app
    router-view
</template>

<style lang="scss">
#app {
  font-family: "Nunito", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;

  @media only screen and (min-width: 768px) {
    p,
    li,
    th,
    td {
      font-size: 21px;
    }
  }

  button.button {
    transition: background-color 0.08s linear, color 0.08s linear,
      box-shadow 0.08s linear;
  }

  button.button.circular {
    border-radius: 1.5rem;
    box-shadow: 0 1px 2px 0 rgba(60, 64, 67, 0.302),
      0 1px 3px 1px rgba(60, 64, 67, 0.149);

    &:hover,
    &:focus {
      box-shadow: 0 1px 3px 0 rgba(60, 64, 67, 0.302),
        0 4px 8px 3px rgba(60, 64, 67, 0.149);
    }
  }
}

button.button.circular.bigger {
  font-size: 1.25rem;
  font-weight: 700;
  height: 3rem;
  line-height: 2rem;
  padding: 0 1.5rem;
}

p.control {
  margin: 0 auto;
  display: flex;
  width: 30rem;
  max-width: 80vw;
}

.hero-head,
.hero-footer {
  padding: 2rem 2rem 0 2rem;
}
</style>
